import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  credentials: Object;

  setCredentials(obj: Object): void {
    this.credentials = obj;
  }

  getCredentials(): Object {
    return this.credentials;
  }
}
