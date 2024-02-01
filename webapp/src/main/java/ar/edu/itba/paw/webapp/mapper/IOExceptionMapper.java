package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Component
@Provider
@Singleton
public class IOExceptionMapper implements ExceptionMapper<IOException> {

    private final MessageSource messageSource;
    @Autowired
    public IOExceptionMapper(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public Response toResponse(IOException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(ErrorDTO.fromError(messageSource.getMessage("exception.internalError", null, LocaleContextHolder.getLocale()),null)).build();
    }
}
