param (
    [int]$inputNumber
)


function Runtest {
     param(
        [int]$sorszam,
        [int]$feedback

    )

    if ($sorszam -gt 0 -and $sorszam -le 10) {
         $inputFile = "$($PSScriptRoot)\input0$($sorszam).txt"
         $outputFile = "$($PSScriptRoot)\output0$($sorszam).txt"
    }

    if ($sorszam -gt 9 -and $sorszam -le 35) {
       $inputFile = "$($PSScriptRoot)\input$($inputNumber).txt"
       $outputFile = "$($PSScriptRoot)\output$($inputNumber).txt"
    }

    #contents of read files
    $input = Get-Content $inputFile -Encoding UTF8 -Raw
    $expectedOutput = Get-Content $outputFile -Raw

    #compile and run program with inputfile
    $program = "$($PSScriptRoot)\Main.java" #proto.java???
    javac $program
    $actualOutput =  java -cp $PSScriptRoot Main $input| Out-String

    
    # Trim the trailing newline from the expected and actual output strings
    #$expectedOutput = $expectedOutput.TrimEnd("`n")
   # $actualOutput = $actualOutput.TrimEnd("`n")
    Write-Host $actualOutput



    #split expected and actual output to array of line
    $expectedOutputLines = $expectedOutput -split "`n"
    $actualOutputLines = $actualOutput -split "`n"

    #test was not successfull
    if ($expectedOutput -ne $actualOutput) {
        Write-Host "Test number $($sorszam) failed!"

        #if we need feedback
        if ($feedback -eq 1) {
               #compare outputs line by line
                for ($i = 0; $i -lt $expectedOutputLines.Length; $i++) {
                    if (($expectedOutputLines[$i] -ne $actualOutputLines[$i])) {
                        Write-Host "Found in line $($i+1):$($actualOutputLines[$i])"
                        Write-Host "Expected in line $($i+1):$($expectedOutputLines[$i])"
                    }
                }
        }
    }

    #test was successfull
    else {
        Write-Host "Test number $($sorszam) passed!"
    }  
}



if ($inputNumber) {
    if ($inputNumber -gt 34 -or $inputNumber -lt 1) {
        Write-Host "Invalid argument!"
    }
    else {
        Runtest -sorszam $inputNumber -feedback 1
    }
} 
else {
    for ($i = 1; $i -le 34; $i++) { Runtest -sorszam $i -feedback 0}
}
