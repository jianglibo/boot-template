Param(
    [parameter(Mandatory=$true)]$DstFolder,
    [parameter(Mandatory=$true)]$RootPackage,
    [string]$InitVersion="0.1.0"
)

$templateProject = $PSCommandPath | Split-Path -Parent

if (-not (Split-Path $DstFolder -IsAbsolute)) {
    $DstFolder = $templateProject | Split-Path -Parent | Join-Path -ChildPath $DstFolder
}

if (Test-Path $DstFolder) {
    "$DstFolder already exists. stop clone!" | Write-Error
} else {
    New-Item -Path $DstFolder -ItemType Directory
}

[array]$wholePackagePath = $RootPackage -split "\."

if ($wholePackagePath.Count -lt 3) {
    "At least 3 segments in package." | Write-Error
}

$packagePathButLast = $wholePackagePath[0..($wholePackagePath.Count - 2)] -join "/"
$lastPackageName = $wholePackagePath[-1]

$resource = "java/main/resources"
$testSource = "src/test/java"

$fixPositionsToCopy = ".gradle","emberworkspace","learning", ".gitignore", ".gitattributes", "build.gradle", "CHANGE.md", "clone.ps1", "gradle.properties.template","README.md"

function Copy-JaveSource {
    Param($srcFolders)
    foreach ($srcFolder in $srcFolders) {
        $s = $templateProject | Join-Path -ChildPath $srcFolder | Join-Path -ChildPath "hello"
        $d = $DstFolder | Join-Path -ChildPath $srcFolder | Join-Path -ChildPath $packagePathButLast
        if (-not (Test-Path $d -PathType Container)) {
            New-Item -Path $d -ItemType Directory -Force
        }
        Copy-Item -Path $s -Destination $d -Recurse
        Rename-Item -Path ($d | Join-Path -ChildPath "hello") -NewName ($d | Join-Path -ChildPath $lastPackageName)

        Get-ChildItem -Path $d -Recurse `
        | Where-Object {$_ -is [System.IO.FileInfo]} `
        | Where-Object Name -Match "\.java$" `
        | ForEach-Object {$_.FullName} `
        | ForEach-Object {
            $currentFile = $_
            $originLines = Get-Content -Path $currentFile -Encoding UTF8
            $newLines = @()
            $done = $false
            foreach ($line in $originLines) {
                if ($done) {
                    $newLines += $line
                } else {
                    if ($line -match "^package\s+hello;$") {
                        $newLines += "package $RootPackage"
                    } elseif ($line -match "^package\s+hello(\..*)$") {
                        $newLines += "package $RootPackage" + $Matches[1]
                    } else {
                        $newLines += $line
                    }
                }
            }
            $newLines | Out-File $currentFile -Encoding utf8
        }
    }
}

function Copy-FixPositionFiles {
    $fixPositionsToCopy `
    | Select-Object -Property @{n="Path";e={$templateProject | Join-Path -ChildPath $_}},
    @{n="Destination";e={$DstFolder | Join-Path -ChildPath $_ }} `
    | Copy-Item -Recurse | Out-Null
}

Copy-FixPositionFiles
Copy-JaveSource -srcFolder "src/main/java", "src/test/java"

$rs = $templateProject | Join-Path -ChildPath "src/main/resources"
$rd = $DstFolder | Join-Path -ChildPath "src/main"

if (-not (Test-Path $rd -PathType Container)) {
    New-Item -Path $rd -Force
}

Copy-Item -Path $rs -Destination $rd -Recurse | Out-Null