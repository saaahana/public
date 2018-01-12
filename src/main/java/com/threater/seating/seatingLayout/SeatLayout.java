package com.threater.seating.seatingLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.threater.seating.beans.TicketSaleRequest;
import com.threater.seating.beans.TicketSaleResponse;
import com.threater.seating.util.ThreaterSeatingUtil;

/**
 * The Class SeatLayout.
 */
@Component
public class SeatLayout {
	
	/**
	 * Book seats.
	 *
	 * @param request the request
	 * @return the ticket sale response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public TicketSaleResponse bookSeats(TicketSaleRequest request) throws IOException{
		if(ThreaterSeatingUtil.seatlayout.size() <=0){
			System.out.println("before preparing Layout");
			preparingLayout();
		}
		TicketSaleResponse response = new TicketSaleResponse();
		int numberOfSeats = request.getNumberOfTickets();
		if(ThreaterSeatingUtil.numberOfSeatsLeft > numberOfSeats){
			boolean isAllocated = false;
			int numberOfRows = ThreaterSeatingUtil.seatlayout.keySet().size();
			for(int i = 1; i<= numberOfRows; i++){
				List<Integer> sections = ThreaterSeatingUtil.seatlayout.get("Row"+i);
				for(int j = 1; j <= sections.size(); j++){
					if(sections.get(j-1) >= numberOfSeats){
						sections.set(j-1, (sections.get(j-1)-numberOfSeats));
						ThreaterSeatingUtil.numberOfSeatsLeft = ThreaterSeatingUtil.numberOfSeatsLeft - numberOfSeats;
						isAllocated = true;
						response.setMessage("Row "+i+" Section "+j);
						break;
					}
					if(isAllocated){
						break;
					}
				}
				if(isAllocated){
					break;
				}
			}
			if(isAllocated){
				response.setStatus("Success");
				ThreaterSeatingUtil.bookingHistoryDetails.add(request.getName()+" "+response.getMessage());
				response.setMessage(request.getName()+" "+response.getMessage());
			}else {
				response.setStatus("failed");
				response.setMessage(request.getName()+" Call to split party.");
			}
			
			
		}else{
			response.setStatus("failed");
			response.setMessage(request.getName()+" Sorry, we can't handle your party.");
		}
		
		return response;
	}

	/**
	 * Preparing layout.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void preparingLayout() throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = SeatLayout.class.getClassLoader().getResourceAsStream("/theater-layout.properties");
		properties.load(inputStream);
		String seatingLayout= (String) properties.get("seating.layout");
		String[] numberOfRows =  seatingLayout.split(",");
		if(numberOfRows.length > 0){
			for(int i = 1; i<= numberOfRows.length; i++){
				String[] sections = numberOfRows[i-1].split(" ");
				List<Integer> seatCount = null;
				if(sections.length > 0) {
					seatCount = new ArrayList<Integer>();
					for(int j = 0; j< sections.length; j++){
						int seats = Integer.parseInt(sections[j]);
						ThreaterSeatingUtil.numberOfSeatsLeft = ThreaterSeatingUtil.numberOfSeatsLeft+seats;
						seatCount.add(seats);
					}
				}
				ThreaterSeatingUtil.seatlayout.put("Row"+i, seatCount);
			}
		}
	}
	
	/**
	 * Gets the booking history.
	 *
	 * @return the booking history
	 */
	public List<String> getBookingHistory(){
		return ThreaterSeatingUtil.bookingHistoryDetails;
	}
	
	/**
	 * Reset seating layout.
	 */
	public void resetSeatingLayout(){
		ThreaterSeatingUtil.seatlayout.clear();
		ThreaterSeatingUtil.numberOfSeatsLeft = 0;
		ThreaterSeatingUtil.bookingHistoryDetails.clear();
	}
}
