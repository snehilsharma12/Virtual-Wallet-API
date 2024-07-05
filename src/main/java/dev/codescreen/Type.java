/**
 * Type class that defines the various objects that are needed for schema.
 * 
 * @author: Snehil Sharma (snehilsharma12@gmail.com)
 * 
 */

package dev.codescreen;

// helps with automatically creating get, set or tostring methods for the classes
import lombok.Data;

public class Type {

    @Data
    public static class Amount {
        private String amount;
        private String currency;
        private DebitCredit debitOrCredit;

        public Amount() {
            amount = "0";
            currency = "USD";
            debitOrCredit = DebitCredit.CREDIT;
        }

        public Amount(String amount, String currency, DebitCredit debitOrCredit) {

            this.amount = amount;
            this.currency = currency;
            this.debitOrCredit = debitOrCredit;

        }
    }

    public enum DebitCredit {
        DEBIT, CREDIT
    }

    @Data
    public static class Ping {
        private String serverTime;

        public Ping() {
        };
    }

    @Data
    public class Error {
        private String message;
        private String code;

        public Error() {
        }
    }

    @Data
    public static class AuthorizationRequest {
        private String userId;
        private String messageId;
        private Amount transactionAmount;

        public AuthorizationRequest() {
        }

        public AuthorizationRequest(String userId, String messageId, Amount transactionAmount) {
            this.userId = userId;
            this.messageId = messageId;
            this.transactionAmount = transactionAmount;
        }
    }

    @Data
    public static class LoadRequest {
        private String userId;
        private String messageId;
        private Amount transactionAmount;

        public LoadRequest() {
        }

        public LoadRequest(String userId, String messageId, Amount transactionAmount) {
            this.userId = userId;
            this.messageId = messageId;
            this.transactionAmount = transactionAmount;

        }
    }

    @Data
    public static class AuthorizationResponse {
        private String userId;
        private String messageId;
        private ResponseCode responseCode;
        private Amount balance;

        public AuthorizationResponse() {
        }

        public AuthorizationResponse(String userId, String messageId, ResponseCode responseCode, Amount balance) {
            this.userId = userId;
            this.messageId = messageId;
            this.responseCode = responseCode;
            this.balance = balance;
        }
    }

    @Data
    public static class LoadResponse {
        private String userId;
        private String messageId;
        private Amount balance;

        public LoadResponse() {
        }

        public LoadResponse(String userId, String messageId, Amount balance) {

            this.userId = userId;
            this.messageId = messageId;
            this.balance = balance;

        }

    }

    public enum ResponseCode {
        APPROVED, DECLINED
    }

}
