package com.pad.server.base.services.pdf;

import java.io.File;

import com.pad.server.base.entities.PDF;

public interface PdfService {

    public File generatePdf(PDF pdf, String fileName) throws Exception;
}
