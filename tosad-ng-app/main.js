const { app, BrowserWindow, ipcMain } = require('electron')

let win

function createWindow () {
  win = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      nodeIntegration: true
    },
    frame: false
  })

  win.maximize()

  win.loadFile('release/index.html')

  win.on('closed', () => {
    win = null
  })
}

app.on('ready', createWindow)

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  if (win === null) {
    createWindow()
  }
})

ipcMain.on('close-app', (event, arg) => {
  app.quit()
})

ipcMain.on('min-app', (event, arg) => {
  win.minimize()
})