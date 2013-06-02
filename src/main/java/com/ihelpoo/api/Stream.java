package com.ihelpoo.api;

import com.ihelpoo.api.transaction.TransactionBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Component
@Path("/stream")
public class Stream {

    @Autowired
    TransactionBo transactionBo;

    @GET
    @Path("/a")
    public Response savePayment() {

        String result = transactionBo.save();

        return Response.status(200).entity(result).build();

    }

}