package chatlive.listeners;

import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;

/**
 * <h1>Networks - UVG</h1>
 * <h2> Xmpp File Transfer Listener </h2>
 * This Listener will get and send the information of the sended file.
 * 
 * Created By:
 * @author Cristian Fernando Laynez Bachez - 201281
 * @since 2023
 **/

public class XmppFileTransferListener implements FileTransferListener {

    @Override
    public void fileTransferRequest(FileTransferRequest request) {
        System.out.println("Received file transfer request from: " + request.getRequestor());
        System.out.println("File name: " + request.getFileName());
        System.out.println("File size: " + request.getFileSize() + " bytes");
        System.out.println("Description: " + request.getDescription());
        System.out.println("MIME type: " + request.getMimeType());

        request.accept();
    }
    
}
