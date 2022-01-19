<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Port Access Dakar</title>
</head>
<body>
    <object data="${filePath}" type="application/pdf" width="100%" height="900px">
        <embed src="${filePath}" type="application/pdf">
            <div style="text-align: center; font-size: 30px">
                <p>
                    Ce navigateur ne prend pas en charge l'affichage des fichiers PDF. S'il vous plaît télécharger le PDF pour le voir: <a href="${filePath}">Télécharger le PDF</a>.
                </p>
                <br><br>
                <p>
                    This browser does not support viewing PDF files. Please download the PDF to view it: <a href="${filePath}">Download PDF</a>.
                </p>
            </div>
        </embed>
    </object>
</body>
</html>
