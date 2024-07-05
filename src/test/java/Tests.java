
/**
 * Tests implemented to check if the the enpoints are working as expected.
 * 
 * @author: Snehil Sharma (snehilsharma12@gmail.com)
 */

import org.junit.Test;

import dev.codescreen.*;
import dev.codescreen.Type.Amount;
import dev.codescreen.Type.AuthorizationRequest;
import dev.codescreen.Type.AuthorizationResponse;
import dev.codescreen.Type.DebitCredit;
import dev.codescreen.Type.LoadRequest;
import dev.codescreen.Type.LoadResponse;
import dev.codescreen.Type.Ping;
import dev.codescreen.Type.ResponseCode;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.regex.Pattern;

public class Tests {

    @Test
    public void testPing() {
        Controller controller = new Controller();
        Ping ping = controller.ping();
        String expectedFormat = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{9}";
        Pattern pattern = Pattern.compile(expectedFormat);

        assertTrue(pattern.matcher(ping.getServerTime()).matches());

    }

    @Test
    public void testLoadTransaction() {

        String userId = "551";
        String messageId = "123456";
        Controller controller = new Controller();
        Amount transactionAmount = new Amount("100", "USD", DebitCredit.CREDIT);

        LoadRequest loadRequest = new LoadRequest(userId, messageId, transactionAmount);

        LoadResponse response = controller.loadTransaction(messageId, loadRequest);

        assertEquals(userId, response.getUserId());
        assertEquals(messageId, response.getMessageId());
        assertEquals(new Amount("100", "USD", DebitCredit.CREDIT), response.getBalance());
    }

    @Test
    public void testAuthorizeTransaction() {

        String userId = "552";
        String messageId = "123456";
        Controller controller = new Controller();
        Amount transactionAmount = new Amount("100.00", "USD", DebitCredit.CREDIT);

        LoadRequest loadRequest = new LoadRequest(userId, messageId,
                transactionAmount);

        LoadResponse loadResponse = controller.loadTransaction(messageId,
                loadRequest);

        String messageId2 = "123457";
        Amount transactionAmount2 = new Amount("80.00", "USD", DebitCredit.DEBIT);
        AuthorizationRequest authRequest = new AuthorizationRequest(userId, messageId2, transactionAmount2);

        AuthorizationResponse authResponse = controller.authorizeTransaction(messageId2, authRequest);

        assertEquals(userId, authResponse.getUserId());
        assertEquals(messageId2, authResponse.getMessageId());
        assertEquals(ResponseCode.APPROVED, authResponse.getResponseCode());

        assertEquals(new Amount("20.00", "USD", DebitCredit.DEBIT), authResponse.getBalance());
    }

}
