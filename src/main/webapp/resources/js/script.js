function openTab(evt, tabName) {
    let i, tabcontent, tablinks;

    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}

function chooseAccount(userAccountIndex) {
    let x = new XMLHttpRequest();
    x.accountIndex = userAccountIndex;
    x.open("GET", "/cabinet/accounts/" + userAccountIndex, true);
    x.onload = callbackAboutAccount;
    x.send(null);
}

function callbackAboutAccount() {
    let xml = this.responseXML;
    let account = xml.getElementsByTagName("accounts")[0];
    let id = account.getElementsByTagName("id")[0].childNodes[0].nodeValue;
    let money = account.getElementsByTagName("money")[0].childNodes[0].nodeValue;

    let table = document.getElementById("accountInfo");
    if(table.rows.length < 5) {
        for (let i = 0; i < 5; i++){
            let row = table.insertRow();
            row.insertCell();
        }
    }
    table.rows[0].cells[0].innerHTML = "<h style='font-size: 20px'>" + id + "</h>";
    table.rows[1].cells[0].innerHTML = "<h style='font-size: 20px'>" + money+"$" + "</h>";
    table.rows[2].cells[0].innerHTML = "<button style='font-size: 20px;width: 100%' " +
        "onclick='addMoney("+id+","+this.accountIndex+")'>Add money</button>";
    table.rows[3].cells[0].innerHTML = "<button style='font-size: 20px;width: 100%' " +
        "onclick='transferMoney("+id+","+this.accountIndex+")'>Transfer money</button>";
    table.rows[4].cells[0].innerHTML = "<button style='font-size: 20px;width: 100%' " +
        "onclick='closeAccount("+id+")'>Close account</button>";
}

function loadAccounts() {
    let x = new XMLHttpRequest();
    x.open("GET", "/cabinet/accounts", true);
    x.onload = callbackAccountsList;
    x.send(null);
}

function callbackAccountsList() {
    let xml = this.responseXML;
    let accounts = xml.getElementsByTagName("accounts")[0];
    let table = document.getElementById("accounts");
    let rowCount = table.rows.length;
    for (let i = 0; i < rowCount; i++) {
        table.deleteRow(0);
    }

    for (let i = 0; i < accounts.childNodes.length; i++){
        let row = table.insertRow();
        row.insertCell();
        let acc = accounts.childNodes[i];

        table.rows[i].cells[0].innerHTML = "<button style='font-size: 20px;' " +
            "onclick=\"chooseAccount(" + i + ")\">"
            + acc.getElementsByTagName("id")[0].childNodes[0].nodeValue + "</button>"
    }
}

function addAccount() {
    let x = new XMLHttpRequest();
    x.open("POST", "/cabinet/accounts/new", true);
    x.onload = callbackAddAccount;
    x.send(null);
}

function callbackAddAccount() {
    loadAccounts();
    updateOperationHistory();
    updateAllAccountsInfo();
}

function closeAccount(accountID) {
    let x = new XMLHttpRequest();
    x.open("DELETE", "/cabinet/accounts/" + accountID, true);
    x.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    x.onload = callbackCloseAccount;
    x.send(null);
}

function callbackCloseAccount() {
    loadAccounts();
    flushAboutAcc();
    updateOperationHistory();
    updateAllAccountsInfo();
}

function flushAboutAcc() {
    let table = document.getElementById("accountInfo");
    let rowCount = table.rows.length;
    for (let i = 0; i < rowCount; i++) {
        table.deleteRow(0);
    }
    table.removeChild(table.getElementsByTagName("tbody")[0]);
}

function addMoney(accountID, accountIndex){
    let money = prompt("Enter money to add", "0");
    if(!/^\d+$/.test(money)) {alert("Entered value not a number");return;}
    let x = new XMLHttpRequest();
    x.accountIndex = accountIndex;
    let body = "accountID=" + accountID + "&money=" + money;
    x.open("POST", "/cabinet/addMoney", true);
    x.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    x.onload = callbackAddMoney;
    x.send(body);
}

function callbackAddMoney() {
    chooseAccount(this.accountIndex);
    updateOperationHistory();
    updateAllAccountsInfo();
}

function transferMoney(accountFromID, accountFromIndex) {
    let accountToID = prompt("Enter account id to transfer");
    if(!/^\d+$/.test(accountToID)) {alert("Entered value not a number");return;}
    let money = prompt("Enter money to transfer", "0");
    if(!/^\d+$/.test(money)) {alert("Entered value not a number");return;}
    let x = new XMLHttpRequest();
    x.accountIndex = accountFromIndex;
    let body = "from=" + accountFromID + "&to=" + accountToID + "&money=" + money;
    x.open("POST", "/cabinet/transferMoney", true);
    x.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    x.onload = callbackTransferMoney;
    x.send(body);
}

function callbackTransferMoney(){
    if(this.status === 204){
        alert("You cant transfer more money than you have");return;
    }
    chooseAccount(this.accountIndex);
    updateOperationHistory();
    updateAllAccountsInfo();
}

function showOperationHistory() {
    let x = new XMLHttpRequest();
    x.open("GET", "/cabinet/operations", true);
    x.onload = callbackHistory;
    x.send(null);
}

function updateOperationHistory() {
    let x = new XMLHttpRequest();
    x.open("GET", "/cabinet/operations", true);
    x.onload = callbackHistoryUpdateOnly;
    x.send(null);
}

function callbackHistory() {
    let history = document.getElementById("historyTable");
    let allAcc = document.getElementById("allAccountsTable");

    let a = callbackHistoryUpdateOnly.bind(this);
    a();

    allAcc.style.display = "none";
    history.style.display = "block";
}

function callbackHistoryUpdateOnly() {
    let history = document.getElementById("historyTable");

    let rowCount = history.rows.length;
    for (let i = 0; i < rowCount; i++) {
        history.deleteRow(0);
    }

    let xml = this.responseXML;
    let operations = xml.getElementsByTagName("operations")[0];
    for (let i = 0; i < operations.childNodes.length; i++){
        let row = history.insertRow();
        row.insertCell();
        let oper = operations.childNodes[i];
        history.rows[i].cells[0].innerHTML = "<h style='font-size: 20px'>" +
            oper.getElementsByTagName("info")[0].childNodes[0].nodeValue +
            "</h>"
    }
}

function showAllAccountsInfo() {
    let x = new XMLHttpRequest();
    x.open("GET", "/cabinet/accounts", true);
    x.onload = callbackAllAccountsInfo;
    x.send(null);
}

function updateAllAccountsInfo() {
    let x = new XMLHttpRequest();
    x.open("GET", "/cabinet/accounts", true);
    x.onload = callbackAllAccountsInfoUpdateOnly;
    x.send(null);
}

function callbackAllAccountsInfo() {
    let history = document.getElementById("historyTable");
    let allAcc = document.getElementById("allAccountsTable");

    let a = callbackAllAccountsInfoUpdateOnly.bind(this);
    a();

    history.style.display = "none";
    allAcc.style.display = "block";
}

function callbackAllAccountsInfoUpdateOnly() {
    let allAcc = document.getElementById("allAccountsTable");

    let rowCount = allAcc.rows.length;
    for (let i = 0; i < rowCount; i++) {
        allAcc.deleteRow(0);
    }

    let xml = this.responseXML;
    let accounts = xml.getElementsByTagName("accounts")[0];
    for (let i = 0; i < accounts.childNodes.length; i++){
        let row = allAcc.insertRow();
        row.insertCell();
        let acc = accounts.childNodes[i];

        allAcc.rows[i].cells[0].innerHTML = "<h style='font-size: 20px'>"
            + acc.getElementsByTagName("id")[0].childNodes[0].nodeValue + ": "
            + acc.getElementsByTagName("money")[0].childNodes[0].nodeValue + "$" +
            "</h>"
    }
}


