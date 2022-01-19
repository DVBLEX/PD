$datetime = Get-Date
$output_log_file_path = "C:\scripts\output.txt"
$response_times_file_path = "C:\scripts\response_times.txt"
$connection_uptime_log_file_path = "C:\scripts\connection_uptime.txt"
$isLog = 1
$url = "https://apps.agsparking.com:8443/padanpr/system/uptime/check"

$scriptExecutionPeriodInSeconds = 60
$btDowntimeSecondsLimit = 180 # 180 seconds by default. the value is updated when API is called
$btUptimeSecondsLimit = 180 # 180 seconds by default. the value is updated when API is called
$isIISServerEnabled = -1
$iisStatus = ''

Function Check-IISStatus {
	$iis = Get-WmiObject Win32_Service -Filter "Name = 'W3SVC'" -ComputerName $env:computername

	if ($iis.State -eq 'Running') {
        if ($isLog -eq 1) { 
		    Write-Output "IIS is running on $env:computername" >> $output_log_file_path
        }
		$script:iisStatus = 'UP'
	} else { 
        if ($isLog -eq 1) {
		    Write-Output "IIS is NOT running on $env:computername" >> $output_log_file_path
        }
		$script:iisStatus = 'DOWN'
	}
}

Function Stop-IIS {
	Write-Output "Stopping IIS on $env:computername" >> $output_log_file_path
	iisreset /stop
	
	if ($LASTEXITCODE -gt 0) {
		Write-Output "Failure Exit Code = $LASTEXITCODE" >> $output_log_file_path
		Write-Output "Retrying..." >> $output_log_file_path
		iisreset /stop
		
		if ($LASTEXITCODE -gt 0) {
			Write-Output "Unable to stop IIS" >> $output_log_file_path
		}
	}
}

Function Start-IIS {
	Write-Output "Starting IIS on $env:computername" >> $output_log_file_path
	iisreset /start
	
	if ($LASTEXITCODE -gt 0) { 
		Write-Output "Failure Exit Code = $LASTEXITCODE" >> $output_log_file_path
		Write-Output "Retrying..." >> $output_log_file_path
		iisreset /start
		
		if ($LASTEXITCODE -gt 0) {
			Write-Output "Unable to start IIS" >> $output_log_file_path
		}
	}
}

Function Save-ResponseTime {
	
	if (!(Test-Path $response_times_file_path)) {
		New-Item $response_times_file_path
		Set-Content $response_times_file_path $datetime
	} else {
		Add-Content -Path $response_times_file_path -Value $datetime
	}
	
	if (!(Test-Path $connection_uptime_log_file_path)) {
		New-Item $connection_uptime_log_file_path
		Set-Content $connection_uptime_log_file_path 0
	} else {
		$connectionUptimeSeconds = Get-Content $connection_uptime_log_file_path -First 1
		$connectionUptimeSeconds = ([long] $connectionUptimeSeconds + $scriptExecutionPeriodInSeconds)
		Set-Content $connection_uptime_log_file_path $connectionUptimeSeconds
	}
}

Function Call-API {

    [Net.ServicePointManager]::SecurityProtocol = 'Tls12'

	$params = @{ status = $script:iisStatus } 
	
	try { 
		$apiResponse = Invoke-WebRequest $url -Method Post -Body $params -UseBasicParsing 
		$apiResponseJSON = $apiResponse.Content | ConvertFrom-Json
		
		$responseCode = ($apiResponseJSON | Select responseCode).responseCode
		$script:btDowntimeSecondsLimit = ($apiResponseJSON.dataList[0] | Select btDowntimeSecondsLimit).btDowntimeSecondsLimit
		$script:btUptimeSecondsLimit = ($apiResponseJSON.dataList[0] | Select btUptimeSecondsLimit).btUptimeSecondsLimit
		$script:isIISServerEnabled = ($apiResponseJSON.dataList[0] | Select isIISServerEnabled).isIISServerEnabled
		
        if ($isLog -eq 1) {
		    Write-Output "API response params:" >> $output_log_file_path
		    Write-Output "responseCode = $responseCode" >> $output_log_file_path
		    Write-Output "btDowntimeSecondsLimit = $btDowntimeSecondsLimit" >> $output_log_file_path
		    Write-Output "btUptimeSecondsLimit = $btUptimeSecondsLimit" >> $output_log_file_path
		    Write-Output "isIISServerEnabled = $isIISServerEnabled" >> $output_log_file_path
        }
		
		if ($responseCode -eq 0) {
			Save-ResponseTime
			
		} else {
			Write-Output "Unexpected response code $responseCode" >> $output_log_file_path
		}
		
	} catch {
		Write-Output "An exception was caught: $($_.Exception)" >> $output_log_file_path
		Set-Content $connection_uptime_log_file_path 0
	}
}

Function Check-ConnectionUptime {
	
	Get-Content $response_times_file_path -Tail 1 -Wait | ForEach-Object {
		
		$timeDiffInSeconds = (New-TimeSpan -Start (Get-Date -Date ($_)) -End $datetime).TotalSeconds
			
		if (($timeDiffInSeconds -gt $script:btDowntimeSecondsLimit) -and ($script:iisStatus -eq 'UP')) {
            if ($isLog -eq 1) {
			    Write-Output 'Check-ConnectionUptime#1' >> $output_log_file_path
            }
			$script:isIISServerEnabled = -1
			Stop-IIS
			
		} elseif (($timeDiffInSeconds -gt $script:btDowntimeSecondsLimit) -and ($script:iisStatus -eq 'DOWN')) {
            if ($isLog -eq 1) {
			    Write-Output 'Check-ConnectionUptime#2' >> $output_log_file_path
			}
            $script:isIISServerEnabled = -1
			
		} elseif (($timeDiffInSeconds -lt $script:btDowntimeSecondsLimit) -and ($script:iisStatus -eq 'DOWN') -and ($script:isIISServerEnabled -eq $TRUE)) {
			if ($isLog -eq 1) {
                Write-Output 'Check-ConnectionUptime#3' >> $output_log_file_path
			}
            $script:isIISServerEnabled = -1
			$connectionUptimeSeconds = Get-Content $connection_uptime_log_file_path -First 1
			$connectionUptimeSeconds = [long] $connectionUptimeSeconds
			
			if ($connectionUptimeSeconds -ge $script:btUptimeSecondsLimit) {
				Start-IIS
			}
		}
		
		if (($script:isIISServerEnabled -eq $TRUE) -and (($script:iisStatus -eq 'DOWN'))) {
            if ($isLog -eq 1) {
			    Write-Output 'Check-ConnectionUptime#4' >> $output_log_file_path
			}
            Start-IIS
			
		} elseif (($script:isIISServerEnabled -eq $FALSE) -and (($script:iisStatus -eq 'UP'))) {
            if ($isLog -eq 1) {
			    Write-Output 'Check-ConnectionUptime#5' >> $output_log_file_path
            }
			Stop-IIS
		}
	
		break
	}
}

if (!(Test-Path $output_log_file_path)) {
	New-Item $output_log_file_path
}

if ($isLog -eq 1) {
    Write-Output "======================================================================================================================================================" >> $output_log_file_path
    Write-Output "Execution of CallBTAPI Script started at $datetime" >> $output_log_file_path
}

Check-IISStatus
Call-API
Check-ConnectionUptime
