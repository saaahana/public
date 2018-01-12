package com.threater.seating.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.threater.seating.beans.TicketSaleRequest;
import com.threater.seating.beans.TicketSaleResponse;
import com.threater.seating.seatingLayout.SeatLayout;

/**
 * The Class TicketBookingController.
 */
@RestController
@RequestMapping("/pre-sale")
public class TicketBookingController {
	
	/** The seat layout. */
	@Autowired
	private SeatLayout seatLayout;
	
	/**
	 * Book tickets.
	 *
	 * @param request the request
	 * @return the ticket sale response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/booktickts", method = RequestMethod.POST)
	public TicketSaleResponse bookTickets(@RequestBody TicketSaleRequest request) throws IOException {
		if(null == request){
			TicketSaleResponse response = new TicketSaleResponse();
			response.setStatus("Failure");
			response.setMessage("Request Should not be empty");
		} else if(null == request.getName()){
			TicketSaleResponse response = new TicketSaleResponse();
			response.setStatus("Failure");
			response.setMessage("Name Should not be empty");
		} else if(request.getNumberOfTickets() == 0){
			TicketSaleResponse response = new TicketSaleResponse();
			response.setStatus("Failure");
			response.setMessage("Please pass Number of seat you want to book for pary");
		}else {
			return seatLayout.bookSeats(request);
		}
		return new TicketSaleResponse();
	}
	
	/**
	 * Booking history.
	 *
	 * @return the list
	 */
	@RequestMapping(value = "/bookingHistory", method = RequestMethod.GET)
	public List<String> bookingHistory() {
		return seatLayout.getBookingHistory();
	}
	
	/**
	 * Reset seating layout.
	 */
	@RequestMapping(value = "/resetSeatingLayout", method = RequestMethod.GET)
	public void resetSeatingLayout()  {
		seatLayout.resetSeatingLayout();
	}
	
	

}
