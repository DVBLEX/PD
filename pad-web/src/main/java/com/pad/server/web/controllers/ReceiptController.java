package com.pad.server.web.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.entities.Account;
import com.pad.server.base.entities.Receipt;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.receipt.ReceiptService;
import com.pad.server.base.services.system.SystemService;

@Controller
public class ReceiptController {

    private static final Logger logger = Logger.getLogger(ReceiptController.class);

    @Autowired
    private AccountService      accountService;

    @Autowired
    private ReceiptService      receiptService;

    @Autowired
    private SystemService       systemService;

    @Value("${system.environment}")
    private String              environment;

    @RequestMapping(value = "/receipt.htm", method = RequestMethod.GET)
    public ModelAndView padReceipt(HttpServletRequest request, @RequestParam(value = "c") String uniqueUrl) {

        String responseSource = "padReceipt#";
        responseSource = responseSource + request.getRemoteAddr();
        ModelAndView mav = new ModelAndView();
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(responseSource);
            builder.append("#Request: ");
            builder.append("[uniqueUrl=");
            builder.append(uniqueUrl);
            builder.append("]");
            logger.info(builder.toString());

            if (environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_LOCAL) || environment.equals(ServerConstants.SYSTEM_ENVIRONMENT_DEV)) {
                mav.addObject("isTestEnvironment", true);
            } else {
                mav.addObject("isTestEnvironment", false);
            }

            Receipt receipt = receiptService.getReceiptByUniqueUrl(uniqueUrl);

            if (receipt == null)
                throw new PADException(ServerResponseConstants.INVALID_URL_CODE, "Lien non valide - Page introuvable.", "invalid URL");

            Account account = accountService.getAccountById(receipt.getAccountId());

            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime receiptLinkExpireDate = LocalDateTime.ofInstant(receipt.getDateCreated().toInstant(), ZoneId.systemDefault());
            receiptLinkExpireDate = receiptLinkExpireDate.plusDays(systemService.getSystemParameter().getReceiptLinkExpireDays());

            if (currentDateTime.isAfter(receiptLinkExpireDate)) {
                String errorMessage = (account == null || account.getLanguageId() == ServerConstants.LANGUAGE_FR_ID) ? "Le lien de réception a expiré."
                    : ServerResponseConstants.RECEIPT_LINK_EXPIRED_TEXT;
                throw new PADException(ServerResponseConstants.RECEIPT_LINK_EXPIRED_CODE, errorMessage, "receipt link expired");
            }

            if (receipt.getDateLock() != null) {
                LocalDateTime receiptLockDate = LocalDateTime.ofInstant(receipt.getDateLock().toInstant(), ZoneId.systemDefault());

                if (currentDateTime.isBefore(receiptLockDate)) {
                    String errorMessage = (account == null || account.getLanguageId() == ServerConstants.LANGUAGE_FR_ID)
                        ? "Le téléchargement du reçu est verrouillé en raison d'un trop grand nombre de tentatives erronées"
                        : ServerResponseConstants.RECEIPT_DOWNLOAD_LOCK_TEXT;
                    throw new PADException(ServerResponseConstants.RECEIPT_DOWNLOAD_LOCK_CODE, errorMessage, "receipt download lock");
                } else {
                    receipt.setLockCountFailed(ServerConstants.ZERO_INT);
                    receipt.setDateLock(null);
                    receiptService.updateReceipt(receipt);
                }
            }
            mav.addObject("receiptNumber", receipt.getNumber());
            mav.addObject("expireLinkDateString",
                ServerUtil.formatDate(ServerConstants.dateFormatDisplayddMMyyyyHHmmss, Date.from(receiptLinkExpireDate.atZone(ZoneId.systemDefault()).toInstant())));
            mav.addObject("accountLanguage", ServerUtil.getLanguageCodeById(account == null ? ServerConstants.LANGUAGE_FR_ID : account.getLanguageId()));
            mav.setViewName("receipt");

        } catch (PADException pade) {
            mav.addObject("message", pade.getResponseText());
            mav.setViewName("message");

            logger.error(responseSource + "#Response: [responseCode=" + pade.getResponseCode() + ", responseText=" + pade.getResponseText() + ", responseSource="
                + pade.getResponseSource() + "]");

        } catch (Exception e) {
            mav.addObject("message", e.getMessage());
            mav.setViewName("message");

            logger.error(responseSource + "###Exception: ", e);
        }

        return mav;
    }

}
