Param(
    [parameter(Mandatory=$true)][string]$DstFile,
    [parameter(Mandatory=$true)][int]$UniqueNumber,
    [parameter(Mandatory=$true)][int]$TotalNumber,
    [parameter(Mandatory=$true)][int]$NumberPerLine
)

$words = @()

0..$UniqueNumber | ForEach-Object {
    $words += (-join ((65..90) + (97..122) | Get-Random -Count 5 | ForEach-Object {[char]$_}))
}

# $stream = New-Object System.IO.StreamWriter($DstFile)
$stream = System.IO.File.CreateText($DstFile)
do {
    $line = (0..$NumberPerLine | ForEach-Object {$words[(Get-Random -Minimum 0 -Maximum $UniqueNumber)]}) -join " "
    $stream.WriteLine($line)
    $TotalNumber -= 1
} while ($TotalNumber -gt 0)

#  | Out-File $DstFile -Encoding ascii

# $stream.Flush()
$stream.close()