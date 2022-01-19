import flask
from flask import request
from datetime import datetime
import json

from escpos import *
import ipaddress

import base64
from io import BytesIO

app = flask.Flask(__name__)
app.config["DEBUG"] = True

class APIResponse:
  def __init__(self, responseCode = 0, responseText = ""):
    self.responseCode = responseCode
    self.responseText = responseText
    now = datetime.now()
    self.responseDate = now.strftime("%Y-%m-%d %H:%M:%S")


@app.route('/', methods=['POST'])
def home():
        p = None
        numOfLetters = 48
        logo = b'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'

        def addPaddingByPercentage(input, percentage):
            numberofLettersToCentre = ((numOfLetters/100) * percentage)
            return addPadding(input, int(numberofLettersToCentre), True)

        def addPadding(input, amount, isBefore):
            if isBefore:
                start = 0
                end = amount
            else:
                start = len(input)
                end = len(input) + amount

            for x in range(start, end):
                if isBefore:
                    input = " " + input
                else:
                    input = input + " "
            return input

        def addMoney(amount, inputString):
            amount = formatMoney(amount)
            currencyLetters = " CFA"
            lengthCurrencyLetters = len(currencyLetters)

            lengthLeftForLetters = numOfLetters - len(amount) - lengthCurrencyLetters - 1
            if len(inputString) > lengthLeftForLetters:
               inputString = inputString[0:lengthLeftForLetters]
            else:
                inputString = addPadding(inputString, lengthLeftForLetters-len(inputString), False)

            return inputString + " " + amount + currencyLetters

        def formatMoney(amount):
            newamount = ""
            if len(amount) <= 3:
                return amount
            i = 1;
            for x in range(len(amount), 0, -1):
                newamount = amount[x -1] + newamount
                if x > 1 and i == 3:
                    newamount = "." + newamount
                    i = 1
                    continue
                else:
                    i = i + 1
            return   newamount

        def printLine():
            # Line
            p.set("center")
            p.textln("------------------------------------")
            p.set("left")

        try:

                     req_data = request.get_json()

                     if not req_data:
                         response = APIResponse(1200,"Missing JSON")
                         return json.dumps(response.__dict__)

                     if 'apiClientId' not in req_data:
                         response = APIResponse(1201,"Missing apiClientId")
                         return json.dumps(response.__dict__)

                     if 'apiSecret' not in req_data:
                         response = APIResponse(1202,"Missing apiSecret")
                         return json.dumps(response.__dict__)

                     if req_data['apiClientId'] != "apiClientId" and req_data['apiSecret'] != "apiSecret":
                         response = APIResponse(1203,"Bad Credentials")
                         return json.dumps(response.__dict__)


                     try:
                             ip = ipaddress.ip_address(req_data['ip'])
                     except:
                             response = APIResponse(1211,"Incorrect IP address format")
                             return json.dumps(response.__dict__)


                     try:
                         p = printer.Network(str(ip))
                     except:
                         response = APIResponse(1212,"Printer not found")
                         return json.dumps(response.__dict__)

                     try:
                         if 'items' in req_data:

                             itemsToPrint = req_data['items']

                             if not itemsToPrint:
                                 response = APIResponse(1213,"Missing items")
                                 return json.dumps(response.__dict__)
                         else:
                             response = APIResponse(1213,"Missing items")
                             return json.dumps(response.__dict__)

                     except:
                             response = APIResponse(1214,"Unknown Missing items")
                             return json.dumps(response.__dict__)



                     if 'total' not in req_data:
                         response = APIResponse(1215,"Missing total")
                         return json.dumps(response.__dict__)
                     if 'money_received' not in req_data:
                         response = APIResponse(1216,"Missing money_received")
                         return json.dumps(response.__dict__)
                     if 'change_given' not in req_data:
                         response = APIResponse(1217,"Missing change_given")
                         return json.dumps(response.__dict__)
                     if 'HT' not in req_data:
                         response = APIResponse(1218,"Missing HT")
                         return json.dumps(response.__dict__)
                     if 'TVA' not in req_data:
                         response = APIResponse(1220,"Missing TVA")
                         return json.dumps(response.__dict__)
                     if 'TTC' not in req_data:
                         response = APIResponse(1221,"Missing TTC")
                         return json.dumps(response.__dict__)
                     if 'receipt_id' not in req_data:
                         response = APIResponse(1222,"Missing receipt_id")
                         return json.dumps(response.__dict__)
                     if 'date' not in req_data:
                         response = APIResponse(1223,"Missing TTC")
                         return json.dumps(response.__dict__)
                     if 'time' not in req_data:
                         response = APIResponse(1224,"Missing time")
                         return json.dumps(response.__dict__)

                     #Print image
                     p.set("center")
                     p.image(BytesIO(base64.b64decode(logo)))
                     p.ln()


                     # Address
                     p.set("left")
                     p.textln(addPaddingByPercentage("AGS Parking",25))
                     p.textln(addPaddingByPercentage("Rocade Fann Bel Air",25))
                     p.textln(addPaddingByPercentage("Dakar",25))
                     p.textln(addPaddingByPercentage("+221 33 832 61 61",25))


                     # Line
                     printLine()

                     vrnline = ""
                     if 'vrn' in req_data and req_data['vrn']:
                         if len(req_data['vrn']) >  numOfLetters:
                             lengthLeftForLetters = numOfLetters - len(vrnline) - 3
                             vrnline = vrnline + req_data['vrn'][0:lengthLeftForLetters] + "..."
                         else:
                             vrnline = req_data['vrn']

                     if 'company' in req_data and req_data['company']:
                         if len(vrnline) > 0 and len(vrnline) < (numOfLetters - 3):
                             vrnline = vrnline + " - "

                         if len(vrnline) + len(req_data['company']) >  numOfLetters:
                             lengthLeftForLetters = numOfLetters - len(vrnline) - 3
                             vrnline = vrnline + req_data['company'][0:lengthLeftForLetters] + "..."
                         else:
                             vrnline = vrnline + req_data['company']

                     if vrnline:
                         p.textln(vrnline)
                         printLine()



                     for i in itemsToPrint:
                         p.textln(addMoney(i['amount'], i['description']))



                     printLine()
                     p.textln(addMoney(req_data['total'], "TOTAL"))
                     printLine()

                     p.textln(addMoney(req_data['money_received'], "RECU ESPECES"))
                     p.textln(addMoney(req_data['change_given'], "RENDU ESPECES"))


                     p.ln(2)
                     headingTAUX = "TAUX"
                     headingHT = " H.T."
                     headingTVA = "TVA"
                     headingTTC = "T.T.C"

                     output = headingTAUX

                     paddingWanted = ((numOfLetters/100) * 25) - len(output)
                     output = addPadding(output, int(paddingWanted), False)
                     output = output + headingHT

                     paddingWanted = ((numOfLetters/100) * 50) - len(output)
                     output = addPadding(output, int(paddingWanted), False)
                     output = output + headingTVA

                     paddingWanted = ((numOfLetters/100) * 75) - len(output)
                     output = addPadding(output, int(paddingWanted), False)
                     output = output + headingTTC

                     p.textln(output)

                     amountTAUX = "18%"
                     amountHT = formatMoney(req_data['HT'])
                     amountTVA = formatMoney(req_data['TVA'])
                     amountTTC = formatMoney(req_data['TTC'])



                     output = amountTAUX

                     paddingWanted = ((numOfLetters/100) * 25) - len(output) + 1
                     output = addPadding(output, int(paddingWanted), False)
                     output = output + amountHT


                     paddingWanted = ((numOfLetters/100) * 50) - len(output)
                     output = addPadding(output, int(paddingWanted), False)
                     output = output + amountTVA


                     paddingWanted = ((numOfLetters/100) * 75) - len(output)
                     output = addPadding(output, int(paddingWanted), False)
                     output = output + amountTTC

                     p.textln(output)

                     # Barcode
                     #p.ln()
                     #p.ln()
                     #p.barcode('13243546','EAN13',64,4,'','')
                     #p.ln()
                     #p.ln()

                     p.set("left")
                     p.ln()
                     p.textln("Receipt ID: " +  req_data['receipt_id'])

                     p.set("left")
                     p.textln()
                     dateTimeSTring = "Date: " + req_data['date']


                     paddingWanted = ((numOfLetters/100) * 50) - len(dateTimeSTring)
                     dateTimeSTring = addPadding(dateTimeSTring, int(paddingWanted), False)
                     dateTimeSTring = dateTimeSTring + "Time: " + req_data['time']
                     p.textln(dateTimeSTring)

                     p.cut()

                     response = APIResponse(0,"Success")
                     return json.dumps(response.__dict__)
        finally:
                if p is not None:
                    p.close()


if __name__ == "__main__":
    app.run()

