package com.pad.server.api.restcontrollers;

import java.time.LocalDate;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pad.server.api.services.api.ApiService;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.ApiResponseJsonEntity;
import com.pad.server.base.services.invoice.InvoiceService;

@RestController
@RequestMapping("/system")
public class SystemRESTController {

    @Autowired
    private ApiService     apiService;

    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value = "/invoices/statements/generate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponseJsonEntity generateInvoicesStatements(@Context HttpServletRequest request, HttpServletResponse response,
        @RequestHeader(name = "authorization") String authHeader, @RequestParam("dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
        @RequestParam("dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) throws PADException, Exception {

        ApiResponseJsonEntity apiResponse = new ApiResponseJsonEntity();

        apiService.validateApiRequest(authHeader, request.getRemoteAddr());

        try {
            if (dateFrom == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "dateFrom is missing");

            if (dateTo == null)
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT, "dateTo is missing");

            if (dateTo.isBefore(dateFrom))
                throw new PADException(ServerResponseConstants.INVALID_REQUEST_FORMAT_CODE, ServerResponseConstants.INVALID_REQUEST_FORMAT_TEXT,
                    "dateFrom has to be before dateTo");

            invoiceService.generateInvoicesStatements(dateFrom.atStartOfDay(), dateTo.atStartOfDay());

            apiResponse.setResponseCode(ServerResponseConstants.SUCCESS_CODE);
            apiResponse.setResponseText(ServerResponseConstants.SUCCESS_TEXT);
            apiResponse.setResponseDate(new Date());

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (PADException pade) {
            throw pade;
        } catch (Exception e) {
            throw e;
        }

        return apiResponse;
    }

}
