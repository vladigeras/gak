import {EventEmitter, Injectable} from '@angular/core';

@Injectable()
export class TransfereService {

  constructor() { }
  public data: EventEmitter<number> = new EventEmitter();

  public transfereData(data: number){
    return this.data.emit(data);
  }

  getData(){
    let temp = this.data;
    this.clearData();
    return temp;
  }

  clearData(){
    this.data = undefined;
  }
}
