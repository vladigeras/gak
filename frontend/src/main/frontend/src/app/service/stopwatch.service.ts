import {Injectable} from '@angular/core';
import * as moment from 'moment';
import {All} from "tslint/lib/rules/completedDocsRule";


@Injectable()
export class StopWatchService {
  public laps: Lap[] = null;

  public momentLap: Lap[]= null

  public momentStartAt: number;
  public momentLapTime: number;

  private startAt: number;
  private lapTime: number;

  constructor() {
    this.reset();


  }

  lap() {
    let timeMs = this.startAt
      ? this.lapTime + this.now() - this.startAt
      : this.lapTime;

    this.laps[this.laps.length - 1].stop(timeMs);
    this.laps.push(new Lap(timeMs));

    let momentMs = this.momentStartAt
      ?  this.now()
      : this.momentLapTime;
    this.momentLap[this.momentLap.length - 1].stop(momentMs);
    this.momentLap.push(new Lap(momentMs))
  }

  now() {
    return _now();
  }

  reset() {
    this.startAt = 0;
    this.lapTime = 0;

    this.momentStartAt = 0;
    this.momentLapTime = 0;

    this.laps = new Array<Lap>();
    this.laps.push(new Lap(0));

    this.momentLap = new Array<Lap>();
    this.momentLap.push(new Lap(0));
  }

  start() {
    this.startAt = this.startAt
      ? this.startAt
      : this.now();

    if (this.momentStartAt){

    }
    else {
      let momentMs = this.now();
      this.momentLap.push(new Lap(momentMs));
    }

    this.momentStartAt = this.momentStartAt
      ? this.momentStartAt
      : this.now();


  }

  stop() {
    let timeMs = this.startAt
      ? this.lapTime + this.now() - this.startAt
      : this.lapTime;

    this.lapTime = timeMs;
    this.laps[this.laps.length - 1].stop(timeMs);

    this.startAt = 0;
  }

  time() {

    return this.lapTime
      + (this.startAt ? this.now() - this.startAt : 0);
  }




  getLaps(){
    return this.momentLap;
  }


}

export class Lap {
  public startMs: number;
  public endMs: number;

  constructor(startMs: number) {
    this.startMs = startMs;
    this.endMs = 0;
  }

  stop(timeMs: number) {
    this.endMs = timeMs;
  }


}



function _now() {
  return moment().unix()*1000;
}
