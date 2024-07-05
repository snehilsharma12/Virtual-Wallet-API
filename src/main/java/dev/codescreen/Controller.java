/**
 * REST controller class that processes and handles the endpoints.
 * 
 * @author: Snehil Sharma (snehilsharma12@gmail.com)
 */

package dev.codescreen;

import dev.codescreen.Type.Ping;
import dev.codescreen.Type.AuthorizationResponse;
import dev.codescreen.Type.LoadResponse;
import dev.codescreen.Type.LoadRequest;
import dev.codescreen.Type.DebitCredit;
import dev.codescreen.Type.ResponseCode;
import dev.codescreen.Type.Amount;
import dev.codescreen.Type.AuthorizationRequest;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
public class Controller {

        private static final Logger logger = LogManager.getLogger(Controller.class);

        // datastructure to hold all the userId and associated balance
        private static Map<String, Amount> balances = new HashMap<>();

        /**
         * Processes the ping request and returns the current date-time
         * 
         * @return = returns the current date-time
         */
        @GetMapping("/ping")
        public Ping ping() {

                Ping ping = new Ping();

                ping.setServerTime(LocalDateTime.now().toString());

                return ping;
        }

        /**
         * Processes the authorizationRequest and returns an authorizationResponse.
         * 
         * @param messageId = the message ID extracted from the path
         * @param request   = the AuthorizationRequest object parsed from the request
         *                  body
         * @return = returns the AuthorizationResponse. {userID, messageID,
         *         APPROVED/DENIED, balance=>Amount}
         */
        @PutMapping("/authorization/{messageId}")
        public AuthorizationResponse authorizeTransaction(@PathVariable("messageId") String messageId,
                        @RequestBody AuthorizationRequest request) {

                try {
                        // Get the Amount object for the userId if exists or get a default Amount of 0
                        // USD
                        Amount Amtbalance = balances.getOrDefault(request.getUserId(),
                                        new Amount());

                        // Get the userId's balance numerical value
                        BigDecimal balance = new BigDecimal(Amtbalance.getAmount());

                        // Get the currency associated
                        String accountCurrency = Amtbalance.getCurrency();

                        // get the transaction amount object
                        Amount transactionAmount = request.getTransactionAmount();

                        // check if the transaction currency matches the userId's amount currency
                        if (!transactionAmount.getCurrency().equals(accountCurrency)) {

                                logger.info("Transaction declined for user ID: {} and messageId: {}",
                                                request.getUserId(),
                                                request.getMessageId());

                                // return DECLINED if currencies don't match
                                return new AuthorizationResponse(request.getUserId(), messageId,
                                                ResponseCode.DECLINED, Amtbalance);

                        }

                        // Get the transaction numerical value
                        BigDecimal transactionValue = new BigDecimal(request.getTransactionAmount().getAmount());

                        // check if the transaction is DEBIT and if the userID's amount balance is
                        // enough
                        if (request.getTransactionAmount().getDebitOrCredit().equals(DebitCredit.DEBIT)
                                        && balance.compareTo(transactionValue) < 0) {

                                logger.info("Transaction declined for user ID: {} and messageId: {}",
                                                request.getUserId(),
                                                request.getMessageId());

                                // return DECLINED if the check fails
                                return new AuthorizationResponse(request.getUserId(), messageId,
                                                ResponseCode.DECLINED, Amtbalance);
                        }

                        // calculate the new balance after processing the authorization
                        balance = balance.subtract(new BigDecimal(transactionAmount.getAmount()));

                        // create a new amount object with the updated balance
                        Amount newAmount = new Amount(balance.toString(), accountCurrency, DebitCredit.DEBIT);

                        // update the userId's amount in the balances
                        balances.put(request.getUserId(), newAmount);

                        // return APPROVED
                        return new AuthorizationResponse(request.getUserId(), messageId,
                                        ResponseCode.APPROVED, newAmount);

                }

                catch (Exception e) {
                        System.out.println(e);
                }

                // return DECLINED
                return new AuthorizationResponse(request.getUserId(), messageId,
                                ResponseCode.DECLINED, new Amount());

        }

        /**
         * Processes the loadRequest and retuens a loadResponse
         * 
         * @param messageId = the message ID extracted from the path
         * @param request   = the loadRequest object parsed from the request
         *                  body
         * @return = returns the loadResponse. {userID, messageID, balance=>Amount}
         */
        @PutMapping("/load/{messageId}")
        public LoadResponse loadTransaction(@PathVariable("messageId") String messageId,
                        @RequestBody LoadRequest request) {

                try {
                        // get the transaction amount object
                        Amount transactionAmount = request.getTransactionAmount();

                        // get the userId amount abject if exist or get default value = 0 USD
                        Amount Amtbalance = balances.getOrDefault(request.getUserId(), new Amount());

                        // check if userId exists in balances and if the transaction currency matches
                        // the userId amount
                        if ((balances.containsKey(request.getUserId()))
                                        && (!transactionAmount.getCurrency().equals(Amtbalance.getCurrency()))) {

                                // return unchanged balance if transaction currency mismatch
                                return new LoadResponse(request.getUserId(), messageId, Amtbalance);
                        }

                        // if userId does not exist => create new zero amount object in transaction
                        // currency
                        else if (!balances.containsKey(request.getUserId())) {

                                Amtbalance = new Amount(BigDecimal.ZERO.toString(), transactionAmount.getCurrency(),
                                                DebitCredit.CREDIT);
                        }

                        // get the numerical value of the userId amount balance
                        BigDecimal balance = new BigDecimal(Amtbalance.getAmount());

                        // get the userId currency
                        String accountCurrency = Amtbalance.getCurrency();

                        // update the balance after processing the transaction amount
                        balance = balance.add(new BigDecimal(transactionAmount.getAmount()));

                        // create a new amount object with the updated balance
                        Amount newAmount = new Amount(balance.toString(), accountCurrency, DebitCredit.CREDIT);

                        // update the userId amount in the balances
                        balances.put(request.getUserId(), newAmount);

                        // return the updated userId amount
                        return new LoadResponse(request.getUserId(), messageId, newAmount);

                }

                catch (Exception e) {
                        System.out.println(e);
                }

                return new LoadResponse(request.getUserId(), messageId, new Amount());

        }

}
