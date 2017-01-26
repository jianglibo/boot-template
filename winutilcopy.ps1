Param(
    [parameter(Mandatory=$true)][string]$hadoopFolder,
    [parameter(Mandatory=$true)][string]$winutilFolder
)

# git clone https://github.com/steveloughran/winutils.git

$sbin = $winutilFolder | Join-Path -ChildPath "bin"
$tbin = $hadoopFolder | Join-Path -ChildPath "bin"

$bothExists = (Test-Path $sbin) -and (Test-Path $tbin)

if (-not $bothExists) {
   Write-Host "$hadoopFolder and ${winutilFolder} should both have a bin subfolder." -ForegroundColor  Red
   return
}

[array]$exitsFiles = Get-ChildItem -Path $tbin | ForEach-Object {Split-Path -Path $_ -Leaf}

$filesToCopy = Get-ChildItem $sbin | ForEach-Object {Split-Path $_ -Leaf} | Where-Object {$exitsFiles -notcontains $_}

"{0} files are going to copy $filesToCopy" -f $filesToCopy.Count | Write-Host

$filesToCopy | Select-Object @{n="Path";e={Join-Path $sbin -ChildPath $_}}, @{n="Destination";e={Join-Path $tbin -ChildPath $_}} | Copy-Item