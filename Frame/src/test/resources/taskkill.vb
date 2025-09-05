

TaskKill = CreateObject("WScript.Shell").Run("taskkill /f /im chromedriver.exe", 0, True)
TaskKill = CreateObject("WScript.Shell").Run("taskkill /f /im chrome.exe", 0, True)
TaskKill = CreateObject("WScript.Shell").Run("taskkill /f /im iexplore.exe", 0, True)
TaskKill = CreateObject("WScript.Shell").Run("taskkill /f /im IEDriverServer.exe", 0, True)
TaskKill = CreateObject("WScript.Shell").Run("taskkill /f /im Winium.Desktop.Driver.exe",0,True)
