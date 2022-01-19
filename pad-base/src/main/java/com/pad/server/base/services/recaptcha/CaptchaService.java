package com.pad.server.base.services.recaptcha;

import com.pad.server.base.exceptions.PADException;

public interface CaptchaService {

    public void processResponse(final String response) throws PADException;

    public String getReCaptchaSite();

    public String getReCaptchaSecret();
}
