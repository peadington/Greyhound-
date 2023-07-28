import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from 'app/shared/services/api.service';
import { UtilityService } from '../../../shared/services/utility.service';

@Injectable({
  providedIn: 'root'
})
export class GreyhoundService {

  displayedColumns: any[] = ['icon', 'date', 'track', 'distance', 'grade', 'stmhcp', 'position', 'winTime', 'weight', 'calcTm', 'rating', 'prizeMoney', 'comment', 'action'];
  displayedRaceColumns: any[] = ['raceUrl', 'date', 'distance', 'position', 'weight', 'winTime', 'stmHop', 'calcTm']
  constructor(
    private _formBuilder: FormBuilder,
    private _apiUrl: ApiService,
    private _utilityService: UtilityService
  ) { }

  greyhoundFilterForm(element?: any): FormGroup {
    const date = new Date(), y = date.getFullYear(), m = date.getMonth();
    const fromDate = new Date(y, m - 1, date.getDate());
    const toDate = new Date(y, m, date.getDate());
    return this._formBuilder.group({
      fromDate: [element ? element.fromDate : fromDate, [Validators.required]],
      toDate: [element ? element.toDate : toDate, [Validators.required]],
      track: [element ? element.track : null, [Validators.required]],
      trainer: [element ? element.trainer : null, [Validators.required]],
      greyhoundId: [element ? element.greyhoundId : null, [Validators.required]],
      grade: [element ? element.grade : 'all', [Validators.required]],
      distance: [element ? element.distance : '0', [Validators.required]]
    })
  }

  greyhoundCompareFilterForm(element?: any): FormGroup {
    const date = new Date(), y = date.getFullYear(), m = date.getMonth();
    const fromDate = new Date(y, m - 1, date.getDate());
    const toDate = new Date(y, m, date.getDate());
    return this._formBuilder.group({
      fromDate: [fromDate, [Validators.required]],
      toDate: [toDate, [Validators.required]],
      track: ['', [Validators.required]],
      trainer: ["", [Validators.required]],
      distance: "0",
      greyhound: ['', [Validators.required]],
      chartKind: ['stmhcp', [Validators.required]]
    })
  }

  getTracks() {
    return this._apiUrl.get('track/get/all')
  }

  getGrade() {
    return this._apiUrl.get('race/grade')
  }

  getDistance() {
    return this._apiUrl.get('race/distance')
  }

  getGreyhoundData(data: any) {
    return this._apiUrl.post(data, `greyhound/search`)
  }

  getGreyhoundGraphData(data: any) {
    return this._apiUrl.post(data, `greyhound/graph`)
  }

  getRaceDetails(data: any, id: any) {
    return this._apiUrl.post(data, `greyhound/getGreyhoundProfileByGreyhoundId?greyhoundId=${id}`)
  }
  exportExcel(data: any) {
    return this._apiUrl.post(data, `greyhound/export`)
  }
  searchByName(keyword: any) {
    return this._apiUrl.get(`greyhounds/searchByName?keyword=${keyword}`)
  }

  getGreyhoundByTrainer(trainer: any) {
    return this._apiUrl.get(`greyhound/searchByTrainer?keyword=${trainer}`)
  }

  getGreyhoundByTracksAndTrainer(track: any, trainer: any) {
    return this._apiUrl.get(`greyhound/getByTracksAndTrainer?${track}&trainer=${trainer}`)
  }

  getGreyhoundByTrainers(trainer: any) {
    return this._apiUrl.get(`greyhound/searchByTrainers?${trainer}`)
  }

  getGreyhoundByTrackAndTrainers(track: any, trainer: any) {
    return this._apiUrl.get(`greyhound/searchByTrackAndTrainers?${trainer}&track=${track}`)
  }

  getGreyhoundByDateAndTrackAndTrainers(data: any) {
    return this._apiUrl.post(data, `greyhound/searchByDateAndTrackAndTrainers`)
  }

  getGreyhoundInfoByGreyhoundId(greyhoundId: any) {
    return this._apiUrl.get(`greyhound/get?id=${greyhoundId}`)
  }

  greyhoundCompare(data: any) {
    return this._apiUrl.post(data, `greyhound/compare`)
  }

  exportGreyhoundCompare(data: any) {
    let dataList: any = [];
    data.forEach(element => {
      let json = {};
      json['Greyhound Name'] = element.greyhoundName;
      json['Sectional Time'] = element.sTmHcp ? Math.round(element.sTmHcp * 100) / 100 : 0;
      json['Win Time'] = element.winTime ? Math.round(element.winTime * 100) / 100 : 0;
      json['Calculated Time'] = element.calcTime ? Math.round(element.calcTime * 100) / 100 : 0;
      json['Rating'] = element.rating ? Math.round(element.rating * 100) / 100 : 0;
      dataList.push(json);
    });
    this._utilityService.exportToExcel(dataList, "greyhound_compare");
  }
  exportGreyhoundData(data: any) {
    let dataList: any = [];
    data.forEach(element => {
      let json = {};
      json['Date'] = element.date;
      json['Track'] = element.track;
      json['Distance'] = element.distance + "m";
      json['Grade'] = element.grade;
      json['Sectional Time'] = element.sTmHcp;
      json['Position'] = element.position;
      json['Win Time'] = element.winTime;
      json['Weight'] = element.weight;
      json['Calculated Time'] = element.calcTm;
      json['Rating'] = element.rating;
      json['Prize Money'] = "£" + element.prizeMoney;
      json['Comment'] = element.comment;
      dataList.push(json);
    });
    this._utilityService.exportToExcel(dataList, "greyhound_data");
  }

  updateComment(formData: FormData) {
    return this._apiUrl.post(formData, `greyhound/comment/update`)
  }
}