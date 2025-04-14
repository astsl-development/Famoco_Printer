package com;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;

import com.cloudpos.DeviceException;
import com.cloudpos.POSTerminal;
import com.cloudpos.printer.Format;
import com.cloudpos.printer.PrinterDevice;
import com.wizarpos.htmllibrary.PrinterHtmlListener;
// import net.glxn.qrgen.android.QRCode;

public class PX400Printer {
    Context context;
    private PrinterDevice device = null;

    public PX400Printer(Context context) {
        this.context = context;

        if (device == null) {
            device = (PrinterDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.printer");
        }
    }

    public void open() {
        try {
            device.open();
            Util.info("Open Printer succeed!");
        } catch (DeviceException ex) {
            Util.error("Open Printer Failed!");
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            device.close();
            Util.info("Close Printer succeed!");
        } catch (DeviceException ex) {
            Util.error("Close Printer Failed!");
            ex.printStackTrace();
        }
    }

    /**
     * Print text. Synchronous function.
     * @param msg, text to print
     */
    public void printText(String msg) {
        try {
            Format format = new Format();
            format.setParameter(Format.FORMAT_FONT_SIZE, Format.FORMAT_FONT_SIZE_MEDIUM);
            device.printText(format, msg);
            Util.info("Print Text succeed!");
        } catch (DeviceException ex) {
            Util.error("Print Text Failed!");
            ex.printStackTrace();
        }
    }


    /**
     * Print data from asset html file.
     * Asynchronous function, the print is not performed immediately.
     * @param context, activity context
     * @param filename, html file name
     * @param listener, callback when the print is finished
     */
    public void printHtml(Context context, String filename, IHtmlPrintListener listener) {
        try {
            WebView.enableSlowWholeDocumentDraw();
            device.printHTML(context, Util.readAssets(context, filename), new PrinterHtmlListener() {
                @Override
                public void onGet(Bitmap bitmap, int i) {
                   // nothing to do
                }

                @Override
                public void onFinishPrinting(int i) {
                    Util.info("Print Html finished:" + i);
                    listener.onPrintFinished();
                }
            });
        } catch (DeviceException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Print a qr-code containing data. Synchronous function.
     *
     * @param data, qr-code content
     */
    // public void printQrCode(String data) {
    //     Bitmap bitmap = QRCode.from(data).bitmap();
    //     try {
    //         Format format = new Format();
    //         format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER);
    //         device.printBitmap(format, bitmap);
    //         Util.info("Print Bitmap succeed!");
    //     } catch (DeviceException ex) {
    //         Util.error("Print Bitmap Failed!");
    //         ex.printStackTrace();
    //     }
    // }

    public void endPrint() {
        try {
            device.printText("\n\n\n");
        } catch (DeviceException ex) {
            ex.printStackTrace();
        }
    }

    public interface IHtmlPrintListener {
        void onPrintFinished();
    }

}
