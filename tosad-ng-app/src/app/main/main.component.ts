import { Component, OnInit, AfterViewInit } from '@angular/core';
import { HttpService } from '../http.service';
import { DataService } from '../data.service';

declare const M;

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit, AfterViewInit {

  constructor(private _http: HttpService, private _data: DataService) { }

  table: string = "";
  attribute: string = "";
  category: string = "";
  ruletype: string = "";
  operator: string = "";
  values: string[] = [];
  failureMessageText: string = "";
  listLength: number = 2;
  inputType: string = "";
  data: Object = {}
  tables: string[] = [];
  attributes: string[] = [];
  categories: string[] = [];
  businessRuleTypes: string[] = [];
  operators: string[] = [];
  fixedTables: string[] = [];
  fixedAttributes: string[] = [];

  ngOnInit() {
  }  

  ngAfterViewInit() {
    this._http.postRequest("http://localhost:8080/tosad-api/restservices/define", this._data.getCredentials()).subscribe(rdata => {
      if (!rdata['body']) return;
      this.data = rdata['body']
      this.tables = Object.keys(this.data['datatable']);
      this.refresh()
    })
    this.refresh()
  }
 
  sendDefine() {

    // Toast checks
    if (this.table == "") {
      return M.toast({html: "You must select a target table", classes: "errorToast"})
    } else if (this.attribute == "") {
      return M.toast({html: "You must select a target attribute", classes: "errorToast"})
    } else if (this.category == "") {
      return M.toast({html: "You must select a category", classes: "errorToast"})
    } else if (this.ruletype == "") {
      return M.toast({html: "You must select a business rule type", classes: "errorToast"})
    } else if (this.operator == "") {
      return M.toast({html: "You must select a operator", classes: "errorToast"})
    } else if (this.values.includes("") || this.values.includes(null) || this.values.includes(undefined) || this.values == []) {
      return M.toast({html: "You must select/fill all parameter values", classes: "errorToast"})
    } else if (this.failureMessageText == "") {
      return M.toast({html: "You must fill in a failure message", classes: "errorToast"})
    }

    let sendData = {
      "table": this.table,
      "attribute": this.attribute,
      "category": this.category,
      "ruletype": this.ruletype,
      "operator": this.operator,
      "values": this.values,
      "failureMessage": this.failureMessageText,
      "triggerEvent": "BEFORE DELETE OR INSERT OR UPDATE",
      "credentials": this._data.getCredentials()
    }
    this._http.postRequest('http://localhost:8080/tosad-api/restservices/define/saverule', sendData).subscribe(o => {
      if (!o['body']) return;
      if (o['body']['response'] == "successful") {
        this.resetData()
        M.toast({html: "Define rule successful", classes: "okToast"})
      } else {
        M.toast({html: "Define rule failed", classes: "errorToast"})
      }
    })
  }

  resetData() {
    this.table = "";
    this.attribute = "";
    this.category = "";
    this.ruletype = "";
    this.operator = "";
    this.values = [];
    this.failureMessageText = "";
    this.listLength = 2;
    this.inputType = "";
    this.refresh()
  }

  tableChange() {
    this.attributes = Object.keys(this.data['datatable'][this.table]);
    this.attribute = ""
    this.fixedTables = this.tables.slice();
    this.fixedTables.splice(this.fixedTables.indexOf(this.table), 1);
    this.attributeChange()
    this.refresh()
  }

  fixedTablesChange() {
    this.fixedAttributes = Object.keys(this.data['datatable'][this.values[0]]);
    this.values[1] = null;
    this.refresh();
  }

  attributeChange() {
    this.categories = Object.keys(this.data['categories']); 
    this.category = ""
    this.businessRuleTypes = []
    this.ruletype = ""
    this.operator = ""
    this.operators = []
    this.refresh()
  }

  categoryChange() {
    this.businessRuleTypes = Object.keys(this.data['categories'][this.category]);
    this.ruletype = ""
    this.operator = ""
    this.operators = []
    this.refresh()
  }

  businessRuleTypeChange() {
    this.operators = this.data['categories'][this.category][this.ruletype].operators
    this.operator = ""
    this.values = []
    this.inputType = this.data['datatable'][this.table][this.attribute]['type'];
    this.refresh()
  } 

  refresh() {
    setTimeout(() => {
      M.AutoInit()
    }, 10)
  }

  arrayOne(): any[] {
    return [...Array(this.listLength).keys()]
  }
  
  addField() {
    if (this.listLength == 12) return;
    this.listLength += 1
  }

  removeField() {
    if (this.listLength == 2) return;
    this.listLength -= 1
    this.values.splice(-1, 1)
  }

}
