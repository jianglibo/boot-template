Param(
    [parameter(Mandatory=$true)][string]$SourceDir,
    [parameter(Mandatory=$true)][string]$DestDir,
    [string]$ClassPatten="\.java"
)
$javaDir = Get-Item env:JAVA_HOME | Select-Object -ExpandProperty Value
$javac = $javaDir | Join-Path -ChildPath "bin/javac"

$javac | Invoke-Expression