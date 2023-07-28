import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from 'app/shared/services/api.service';
import { UtilityService } from '../../../shared/services/utility.service';

@Injectable({
  providedIn: 'root'
})
export class TrainerService {

  displayedColumns: any[] = ['icon', 'timesRan', 'greyhoundName', 'position1', 'position2', 'position3', 'position4', 'position5', 'position6', 'prizeMoney'];

  constructor(
    private _apiUrl: ApiService,
    private _formBuilder: FormBuilder,
    private _utilityService: UtilityService
  ) { }

  trainerFilterForm(element?: any): FormGroup {
    const date = new Date(), y = date.getFullYear(), m = date.getMonth();
    const fromDate = new Date(y, m - 1, date.getDate());
    const toDate = new Date(y, m, date.getDate());
    return this._formBuilder.group({
      fromDate: [fromDate, [Validators.required]],
      toDate: [toDate, [Validators.required]],
      trainerId: [element ? element.trainerId : "", Validators.required],
      track: [element ? element.track : "", Validators.required],
      distance: [element ? element.distance : '0', Validators.required],
      grade: [element ? element.grade : 'all', Validators.required],
      // chartKind: [element ? element.chartKind : "", Validators.required]
    })
  }

  createFilterForm(element?: any): FormGroup {
    return this._formBuilder.group({
      trainerId: [element ? [element.trainerId] : "", Validators.required],
      track: [element ? [element.track] : "", Validators.required],
      chartKind: [element ? [element.chartKind] : "", Validators.required],
      distance: [element ? [element.distance] : null],
      grade: [element ? [element.grade] : null],

    })
  }

  createTrainerCompareFilterForm(): FormGroup<any> {
    const date = new Date(), y = date.getFullYear(), m = date.getMonth();
    const fromDate = new Date(y, m - 1, date.getDate());
    const toDate = new Date(y, m, date.getDate());
    return this._formBuilder.group({
      fromDate: [fromDate, [Validators.required]],
      toDate: [toDate, [Validators.required]],
      track: ['', Validators.required],
      trainer: [[], Validators.required],
      // chartKind: 'stmhcp',
      days: 7
    })
  }

  getTrainerList() {
    return this._apiUrl.get('trainers/get')
  }

  getTrainerListByTracks(tracks) {
    return this._apiUrl.get(`trainers/getByTracks?${tracks}`)
  }
  getTrainerByTrack(track) {
    return this._apiUrl.get(`trainers/getByTracks?tracks=${track}`)
  }

  getTrainerListByTrack(track) {
    return this._apiUrl.get(`trainers/getByTrack?${track}`)
  }

  getTrainerListByDateAndTrack(data: any) {
    return this._apiUrl.post(data, `trainers/getByDateAndTrack`);
  }

  getGreyhoundBytrainer(trainerName: any) {
    return this._apiUrl.get(`greyhound/searchByTrainer?keyword=${trainerName}`)
  }

  getTrainerDetailsByGreyhound(data: any) {
    return this._apiUrl.post(data, `trainers/search`)
  }

  searchByName(keyword: any) {
    return this._apiUrl.get(`trainers/searchByName?keyword=${keyword}`)
  }

  // exportExcel(data: any) {
  //   return this._apiUrl.post(data, `trainers/export`)
  // }
  trainerExport(data) {
    return this._apiUrl.post(data, `trainers/export`)
  }
  exportTrainerData(data) {
    let dataList: any = [];
    data.forEach(element => {
      let json = {};
      json['Greyhound Name'] = element.greyhoundName;
      json['Position 1'] = element.position1;
      json['Position 2'] = element.position2;
      json['Position 3'] = element.position3;
      json['Position 4'] = element.position4;
      json['Position 5'] = element.position5;
      json['Position 6'] = element.position6;
      json['Prize Money'] = "£" + element.prizeMoney;
      json['Times Ran'] = element.timesRan;
      dataList.push(json);
    });
    this._utilityService.exportToExcel(dataList, "trainer_data");
  }

  getChartData(data: any) {
    return this._apiUrl.post(data, `trainers/get/graph`);
  }
  getDistanceData() {
    return this._apiUrl.get(`race/distance`);
  }
  getGradeData() {
    return this._apiUrl.get(`race/grade`);
  }

  trainerCompare(data: any) {
    return this._apiUrl.post(data, `trainer/compare`)
  }

  exportTrainerCompare(data: any) {
    let dataList: any[] = [];
    let trainersData = Object.keys(data);
    trainersData.forEach(element => {
      let json = {};
      json['Trainer'] = element;
      json['Wins'] = data[element].win;
      json['Prize'] = data[element].prize;
      dataList.push(json);
    });
    this._utilityService.exportToExcel(dataList, "trainer_compare_" + new Date().getTime());
  }
}
