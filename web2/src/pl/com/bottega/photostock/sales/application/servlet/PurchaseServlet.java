package pl.com.bottega.photostock.sales.application.servlet;

import pl.com.bottega.photostock.sales.model.Offer;
import pl.com.bottega.photostock.sales.api.PurchaseProcess;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Slawek on 19/04/16.
 */
@WebServlet(name="Purchase", displayName="Purchase Servlet", urlPatterns = {"/purchase"}, loadOnStartup=1)
public class PurchaseServlet extends HttpServlet {

    private PurchaseProcess purchaseProcess = new PurchaseProcess();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String prodNr = req.getParameter("prodNr");
        String reservationNr = req.getParameter("resNr");

        purchaseProcess.add(reservationNr, prodNr);
        Offer offer = purchaseProcess.calculateOffer(reservationNr);


        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.println(offer.getTotalCost().toString());
        out.println(prodNr + " " + reservationNr);
        out.close();
    }
}
