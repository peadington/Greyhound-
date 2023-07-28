import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Pagination } from 'app/shared/interfaces/pagination.interface';
import { UtilityService } from 'app/shared/services/utility.service';
import { environment } from 'environments/environment';
import { GreyhoundService } from '../greyhound/greyhound.service';
import { TrainerService } from './trainer.service';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-trainer',
  templateUrl: './trainer.component.html',
  styleUrls: ['./trainer.component.scss']
})
export class TrainerComponent implements OnInit {
  displayedColumns: any[] = [];
  trainerData: any[] = [];
  dataList: any[] = [];
  dataSource: any[] = [];
  typeData: any[] = ['Cumlative', 'Distances', 'Grades']
  trainer: any;
  pagination: Pagination;
  formGroup: FormGroup
  isLoading: boolean = false;
  trackList: any;
  gradeData: any;
  distanceData: any
  trainerDataExport: any
  curruncySymbol = environment.curruncySymbol
  selectedTrainer: any;

  searchTrainerFormControl = new FormControl('');
  filteredTrainerOptions: any[] = [];

  constructor(
    private _trainerService: TrainerService,
    public _utilityService: UtilityService,
    private _router: Router,
    private _greyhoundService: GreyhoundService
  ) {
    // this.filteredOptions = this.searchTrainerFormControl.valueChanges.pipe(
    //   startWith(''),
    //   map(value => this._filter(value || '')),
    // );
  }

  // private _filter(value: string): string[] {
  //   const filterValue = value.toLowerCase();

  //   return this.options.filter(option => option.name.toLowerCase().includes(filterValue));
  // }


  async ngOnInit(): Promise<void> {
    this.formGroup = this._trainerService.trainerFilterForm();
    await this.getAllTracks();
    await this.getAllDistance();
    await this.getAllGrade();
    this.pagination = this._utilityService.pagination
    this.displayedColumns = this._trainerService.displayedColumns;
    if (sessionStorage.getItem("trainer")) {
      this.formGroup = this._trainerService.trainerFilterForm(JSON.parse(sessionStorage.getItem("trainer")));
      await this.onSelectionChangeTrack(this.formGroup.controls.track.value);
      await this.onSelectionChangeTrainer(this.formGroup.controls.trainerId.value);
      this.search()
    }
  }

  async onSelectionChangeType(value) {
    this.formGroup.controls.grade.setValue(null);
    this.formGroup.controls.distance.setValue(null);
  }

  async onInputTrainer() {
    await this.searchTrainersByName(this.searchTrainerFormControl.value);
  }

  async searchTrainersByName(filterValue: any) {
    await this._trainerService.searchByName(filterValue).then((response: any) => {
      if (response && response.body.status === 'OK') {
        this.filteredTrainerOptions = response.body.data;
      } else {
        this.filteredTrainerOptions = [];
      }
    })
  }

  optionSelectedTrainer(greyhound) {
    let element = this.filteredTrainerOptions.find(item => item.name === greyhound);
    if (element) {
      this.formGroup.controls.trainerId.setValue(element.id);
      this.search();
    }
  }

  // async getTrainerList() {
  //   this.isLoading = true;
  //   this._trainerService.getTrainerList().then((response: any) => {
  //     this.trainerData = response.body.data;
  //     this.trainerData.sort(function(a, b){
  //       if(a.name < b.name) { return -1; }
  //       // if(a.firstname > b.firstname) { return 1; }
  //       return 0;
  //   })
  //     this.isLoading = false;
  //   })
  // }

  onFromDateChange(date) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      fromDate: date,
      toDate: data.toDate,
      track: [data.track]
    }
    this.getTrainersByFilter(requestBody);
  }

  onToDateChange(date) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      fromDate: data.fromDate,
      toDate: date,
      track: [data.track]
    }
    this.getTrainersByFilter(requestBody);
  }

  async onSelectionChangeTrack(track: any) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      track: [track],
      fromDate: data.fromDate,
      toDate: data.toDate
    }
    await this.getTrainersByFilter(requestBody);
  }

  async getTrainersByFilter(requestBody: any) {
    this.isLoading = true;
    this.trainerData = [];
    await this._trainerService.getTrainerListByDateAndTrack(requestBody).then((response: any) => {
      this.trainerData = response.body.data;
      this.trainerData.sort(function (a, b) {
        if (a.name < b.name) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    })
  }

  async getAllTracks() {
    this.isLoading = true;
    await this._greyhoundService.getTracks().then((response: any) => {
      this.trackList = response.body.data;
      this.trackList.sort(function (a, b) {
        if (a.name < b.name) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    })
  }

  /**
   * Get next page data
   * 
   * @param page 
   */
  getNextPageData(page: any) {
    this.pagination.currentPage = page.pageIndex + 1;
    this.pagination.perPage = page.pageSize;
    this.search();
  }

  reset() {
    this.formGroup.reset();
    this.formGroup.controls.distance.setValue('0');
    this.formGroup.controls.grade.setValue('all');
    this.formGroup = this._trainerService.trainerFilterForm();
    this.trainerData = [];
    this.dataSource = [];
    sessionStorage.removeItem("trainer");
  }

  search() {
    this.pagination.data = [];
    this.dataSource = [];
    let data = this.formGroup.getRawValue()
    let trainerData = {
      filter: data,
      pagination: this.pagination
    }
    this.isLoading = true;
    this._trainerService.getTrainerDetailsByGreyhound(trainerData).then((response: any) => {
      this.pagination = response.body.data,
        this.dataSource = this.pagination.data
      this.isLoading = false;
    })
  }

  // downloadFile() {
  //   this.isLoading = true;
  //   const data = this.formGroup.getRawValue();
  //   let fileData = {
  //     "greyhoundId": data.greyhound,
  //     "trainerId": data.trainerId
  //   }
  //   this._trainerService.exportExcel(fileData)
  //     .then((response: any) => {
  //       this.isLoading = false;
  //       if (response && response.body.status == 'OK') {
  //         if (response.body.data.length > 0) {
  //           this._utilityService.exportToExcel(response.body.data, "Trainer-Data");
  //         }
  //       }
  //     }
  //     );
  // }


  trainerGraph() {
    let data = this.formGroup.getRawValue();
    sessionStorage.setItem("trainer", JSON.stringify(data));
    this._router.navigate(['/trainer/graph/' + data.trainerId + '/' + data.track])
  }

  async getAllDistance() {
    await this._trainerService.getDistanceData().then((response: any) => {
      this.distanceData = response.body.data;
      this.distanceData.sort(function (a, b) {
        if (a < b) { return -1; }
        return 0;
      })
    })
  }

  async getAllGrade() {
    await this._trainerService.getGradeData().then((response: any) => {
      this.gradeData = response.body.data;
      this.gradeData.sort(function (a, b) {
        if (a < b) { return -1; }
        return 0;
      })
    })
  }

  export() {
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    this._trainerService.trainerExport(data).then((response: any) => {
      this.trainerDataExport = response.body.data;
      this._trainerService.exportTrainerData(this.trainerDataExport);
      this.isLoading = false;
    });
  }

  async onSelectionChangeTrainer(trainerId) {
    this.selectedTrainer = this.trainerData.find(item => item.id === trainerId);
  }
}
