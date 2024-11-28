# Checking if the application-secrets.properties file exists in src/main/resources directory
if (-Not (Test-Path -Path "./src/main/resources/application-secrets.properties")) {
    Write-Output "File application-secrets.properties not found in src/main/resources directory"
    exit 1
}

# Copy the file to build/libs directory
Write-Output "Copying application-secrets.properties to build/libs"
Copy-Item -Path "./src/main/resources/application-secrets.properties" -Destination "./build/libs/"

# Define the environment name
$envName = "hospital-env12" # Name for the new or existing environment

# Check if the environment exists using eb list and eb use
Write-Output "Checking if environment $envName exists"
$envExists = eb list | Select-String -Pattern $envName

# If the environment doesn't exist, create it
if ($envExists) {
    Write-Output "Environment $envName already exists. Proceeding with deployment."
    Start-Process -NoNewWindow -Wait -FilePath "eb" -ArgumentList "use", $envName
} else {
    Write-Output "Environment $envName not found. Creating new environment."
    Start-Process -NoNewWindow -Wait -FilePath "eb" -ArgumentList "create", $envName
    Start-Process -NoNewWindow -Wait -FilePath "eb" -ArgumentList "use", $envName
}

# Deploy via Elastic Beanstalk CLI with increased timeout
Write-Output "Starting Elastic Beanstalk deploy with increased timeout"
Start-Process -NoNewWindow -Wait -FilePath "eb" -ArgumentList "deploy", "--timeout", "30"

# Deleting application-secrets.properties from build/libs directory after deployment
Write-Output "Removing application-secrets.properties from build/libs"
Remove-Item -Path "./build/libs/application-secrets.properties" -Force

Write-Output "Deploy finished"