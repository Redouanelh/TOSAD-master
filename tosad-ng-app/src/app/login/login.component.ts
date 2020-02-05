import { Component, OnInit, AfterViewInit, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { HttpService } from '../http.service';

declare const M;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, AfterViewInit {

  dbTypes: string[] = ["Oracle"];
  dbType: string = "Oracle";
  host: string = "ondora04.hu.nl";
  port: number = 1521;
  SID: string = "";
  ServiceName: string = "EDUC11";
  username: string = "maurits";
  password: string = "maurits";
  name: string = "voetbal";

  @Output() dataOutput = new EventEmitter<Object>();

  constructor(private router: Router, private _data: DataService, private _http: HttpService) { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    this.refresh();
  }

  refresh() {
    setTimeout(() => {
      M.AutoInit()
    }, 10)
  }

  connect() {
    if (this.host.length == 0) {
      return M.toast({html: "A host is required to connect", classes: "errorToast"})
    } else if (this.port == (null || undefined || 0) || this.port < 1 || this.port > 65565) {
      return M.toast({html: "You must choose a port from 1-65565", classes: "errorToast"})
    } else if (this.SID == "" && this.ServiceName == "") {
      return M.toast({html: "You must fill in either SID or Service name", classes: "errorToast"})
    } else if (this.username == "") {
      return M.toast({html: "You must fill in a username", classes: "errorToast"})
    } else if (this.password == "") {
      return M.toast({html: "You must fill in a password", classes: "errorToast"})
    } else if (this.dbType == "") {
      return M.toast({html: "You must select one of the supported database types", classes: "errorToast"})
    } else if (this.name == "") {
      return M.toast({html: "You must fill in a database name", classes: "errorToast"})
    }

    var sendData: Object = {
      url: `${this.host}:${this.port}${this.SID != "" ? ":" + this.SID : "/" + this.ServiceName}`,
      username: this.username,
      password: this.password,
      type: this.dbType,
      name: this.name
    }

    this._http.postRequest("http://localhost:8080/tosad-api/restservices/define/login", sendData).subscribe(rdata => {
      if (!rdata["body"]) return;
      if (rdata["body"]["error"]) return M.toast({html: rdata["body"]["error"], classes: "errorToast"})
      this._data.setCredentials(sendData);
      this.router.navigate(['/define'])
    })
  }

}