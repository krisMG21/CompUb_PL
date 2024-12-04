package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logic.Log;
import logic.Logic;

@WebServlet("/SetData")
public class SetData extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public SetData(){
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Log.log.info("--Get values from the DB--");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			int value = Integer.parseInt(request.getParameter("value"));
			//Logic.setDataToDB(value);
		} catch (NumberFormatException nfe){
			out.println("-1");
			Log.log.error("Number Format Exception: " + nfe);
		} catch (IndexOutOfBoundsException iobe) {
			out.println("-1");
			Log.log.error("Index out of bounds Exception: " + iobe);
		} catch (Exception e){
			out.println("-1");
			Log.log.error("Exception: " + e);
		} finally{
			out.close();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
