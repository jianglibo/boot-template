Param(
    [parameter(Mandatory=$true)][string]$DstFile,
    [parameter(Mandatory=$true)][int]$UniqueWordNumber,
    [parameter(Mandatory=$true)][int]$TotalWordNumber,
    [parameter(Mandatory=$true)][int]$MaxWordsPerLine
)

[array]$words = 0..$UniqueWordNumber | ForEach-Object {
    -join ((65..90) + (97..122) | Get-Random -Count (Get-Random -Maximum 15 -Minimum 5) | ForEach-Object {[char]$_})
}

if (-not ([System.IO.Path]::IsPathRooted($DstFile))) {
    $DstFile = $PSScriptRoot | Join-Path -ChildPath $DstFile
}

$stream = [System.IO.StreamWriter]$DstFile

do {
    $perline = Get-Random -Minimum 1 -Maximum $MaxWordsPerLine
    [array]$wordsInLine = @()
    if ($TotalWordNumber -lt $perline) {
        $perline = $TotalWordNumber
    }
    for ($i = 0; $i -lt $perline; $i++) {
        $wordsInLine += $words[(Get-Random -Minimum 0 -Maximum $UniqueWordNumber)]
        $TotalWordNumber--
    }
    $stream.WriteLine($wordsInLine -join " ")
} while ($TotalWordNumber -gt 0)

$stream.Flush()
$stream.close()

# verify result
# Get-Content $DstFile | ForEach-Object -Begin {$total = 0} -Process {$total += ($_ -split "\s+").Count} -End {$total} | Select-Object @{n="totalWords";e={$total}}