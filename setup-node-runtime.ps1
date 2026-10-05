$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$zip = Join-Path $root 'nodejs-mobile-v18.20.4-android.zip'
$url = 'https://github.com/nodejs-mobile/nodejs-mobile/releases/download/v18.20.4/nodejs-mobile-v18.20.4-android.zip'
Write-Host 'Descargando Node.js Mobile 18.20.4...'
Invoke-WebRequest -Uri $url -OutFile $zip
$tmp = Join-Path $root '_node_runtime'
if (Test-Path $tmp) { Remove-Item $tmp -Recurse -Force }
Expand-Archive $zip -DestinationPath $tmp -Force
$bin = Join-Path $tmp 'bin\arm64-v8a\libnode.so'
$inc = Join-Path $tmp 'include\node'
if (!(Test-Path $bin)) { throw "No se encontro $bin. Revisa el ZIP descargado." }
Copy-Item $bin (Join-Path $root 'app\libnode\bin\arm64-v8a\libnode.so') -Force
Copy-Item "$inc\*" (Join-Path $root 'app\libnode\include\node') -Recurse -Force
Write-Host 'Runtime ARM64 preparado.'
Write-Host 'Ahora ejecuta npm install --omit=dev en app/src/main/assets/nodejs-project y abre el proyecto en Android Studio.'
