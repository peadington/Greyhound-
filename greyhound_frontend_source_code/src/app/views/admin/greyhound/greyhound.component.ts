import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Pagination } from 'app/shared/interfaces/pagination.interface';
import { UtilityService } from 'app/shared/services/utility.service';
import { environment } from 'environments/environment';
import { TrainerService } from '../trainer/trainer.service';
import { GreyhoundService } from './greyhound.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-greyhound',
  templateUrl: './greyhound.component.html',
  styleUrls: ['./greyhound.component.scss']
})
export class GreyhoundComponent implements OnInit {
  formGroup!: FormGroup;
  displayedColumns: any[] = [];
  dataSource: any[] = [];
  trackData: any[] = [];
  isLoading: boolean = false;
  trainerData: any;
  pagination: Pagination;
  selectedGreyhound: any;
  greyhoundList: any[] = [];
  distanceUnit = environment.distanceUnit
  curruncySymbol = environment.curruncySymbol
  greyhoundDataExport: any
  distanceData: any[] = [];
  gradeData: any[] = [];
  selectedTrainer: any;

  searchGreyhoundFormControl = new FormControl('');
  filteredGreyhoundOptions: any[] = [];
  greyhoundInfo: any;

  constructor(
    private _greyhoundService: GreyhoundService,
    private _route: Router,
    private _trainerService: TrainerService,
    public _utilityService: UtilityService,
    public _activatedRoute: ActivatedRoute
  ) { }

  /**
   * Get greyhound Info
   * 
   * @param greyhoundId 
   */
  async getGreyhoundInfo(greyhoundId: any) {
    await this._greyhoundService.getGreyhoundInfoByGreyhoundId(greyhoundId).then((response: any) => {
      this.greyhoundInfo = response.body.data;
      this.isLoading = false;
    })
  }

  /**
   * On Input
   */
  async onInputGreyhound() {
    await this.searchGreyhoundsByName(this.searchGreyhoundFormControl.value);
  }

  /**
   * Search Greyhounds by
   * 
   * @param filterValue 
   */
  async searchGreyhoundsByName(filterValue: any) {
    await this._greyhoundService.searchByName(filterValue).then((response: any) => {
      if (response && response.body.status === 'OK') {
        this.filteredGreyhoundOptions = response.body.data;
      } else {
        this.filteredGreyhoundOptions = [];
      }
    })
  }

  /**
   * Option Selected Greyhound
   * 
   * @param greyhound 
   */
  async optionSelectedGreyhound(greyhound) {
    let element = this.filteredGreyhoundOptions.find(item => item.name === greyhound);
    if (element) {
      this.formGroup.controls.greyhoundId.setValue(parseInt(element.greyhoundId));
      await this.getGreyhoundInfo(element.greyhoundId);
      const fromDate = new Date("2019-01-01 12:00:00");
      this.formGroup.controls.fromDate.setValue(fromDate);
      this.formGroup.controls.track.setValue([this.greyhoundInfo.track]);
      this.formGroup.controls.trainer.setValue(this.greyhoundInfo.trainerId);
      await this.onSelectionChangeTrack(this.formGroup.controls.track.value);
      await this.onSelectionChangeTrainer(this.formGroup.controls.trainer.value);
      this.getGreyhoundData();
    }
  }

  /**
   * NgOnInt
   */
  async ngOnInit() {
    this.formGroup = this._greyhoundService.greyhoundFilterForm();
    await this.getAllTracks();
    await this.getAllDistance();
    await this.getAllGrade();
    this.pagination = this._utilityService.pagination;
    if (this._activatedRoute.snapshot.params.greyhoundId) {
      this.formGroup.controls.greyhoundId.setValue(parseInt(this._activatedRoute.snapshot.params.greyhoundId));
      await this.getGreyhoundInfo(this._activatedRoute.snapshot.params.greyhoundId);
      const fromDate = new Date("2019-01-01 12:00:00");
      this.formGroup.controls.fromDate.setValue(fromDate);
      this.formGroup.controls.track.setValue([this.greyhoundInfo.track]);
      this.formGroup.controls.trainer.setValue(this.greyhoundInfo.trainerId);
      await this.onSelectionChangeTrack(this.formGroup.controls.track.value);
      await this.onSelectionChangeTrainer(this.formGroup.controls.trainer.value);
      this.getGreyhoundData();
    }
    if (!this._activatedRoute.snapshot.params.greyhoundId) {
      if (sessionStorage.getItem("greyhound")) {
        this.formGroup = this._greyhoundService.greyhoundFilterForm(JSON.parse(sessionStorage.getItem("greyhound")));
        await this.onSelectionChangeTrack(this.formGroup.controls.track.value);
        await this.onSelectionChangeTrainer(this.formGroup.controls.trainer.value);
        this.onSelectionChangeGreyhound(this.formGroup.controls.greyhoundId.value);
        this.getGreyhoundData();
      }
    }
    this.displayedColumns = this._greyhoundService.displayedColumns;
  }

  /**
   * Get all tracks
   */
  async getAllTracks() {
    this.isLoading = true;
    await this._greyhoundService.getTracks().then((response: any) => {
      this.trackData = response.body.data;
      this.trackData.sort(function (a, b) {
        if (a.name < b.name) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    })
  }

  /**
   * Get All Distance
   */
  async getAllDistance() {
    await this._trainerService.getDistanceData().then((response: any) => {
      this.distanceData = response.body.data;
      this.distanceData.sort(function (a, b) {
        if (a < b) { return -1; }
        return 0;
      })
    })
  }

  /**
   * Get all Grade
   */
  async getAllGrade() {
    await this._trainerService.getGradeData().then((response: any) => {
      this.gradeData = response.body.data;
      this.gradeData.sort(function (a, b) {
        if (a < b) { return -1; }
        return 0;
      })
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
    this.getGreyhoundData();
  }

  /**
   * On Selection Change Track
   * 
   * @param track 
   */
  async onSelectionChangeTrack(track: any) {
    const data = this.formGroup.getRawValue();
    let requestBody = {
      track: track,
      fromDate: data.fromDate,
      toDate: data.toDate
    }
    await this.getTrainersByFilter(requestBody);
  }

  async onFromDateChange(date) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      track: data.track,
      fromDate: date,
      toDate: data.toDate
    }
    await this.getTrainersByFilter(requestBody);
  }

  async onToDateChange(date) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      track: data.track,
      fromDate: data.fromDate,
      toDate: date
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

  /**
   * On Selection Change Trainer
   * 
   * @param trainerId 
   */
  async onSelectionChangeTrainer(trainerId) {
    this.selectedTrainer = this.trainerData.find(item => item.id === trainerId);
    let data = this.formGroup.getRawValue();
    let requestBody = {
      fromDate: data.fromDate,
      toDate: data.toDate,
      trainers: [trainerId],
      track: data.track
    }
    await this.getGreyhoundsByFilter(requestBody);
  }

  async getGreyhoundsByFilter(requestBody: any) {
    this.isLoading = true;
    this.greyhoundList = [];
    await this._greyhoundService.getGreyhoundByDateAndTrackAndTrainers(requestBody).then((response: any) => {
      this.greyhoundList = response.body.data;
      this.greyhoundList.sort(function (a, b) {
        if (a.greyhoundName < b.greyhoundName) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    })
  }

  /**
   * On Selection Change Greyhound
   * 
   * @param greyhound 
   */
  onSelectionChangeGreyhound(greyhound) {
    this.selectedGreyhound = this.greyhoundList.find(item => item.greyhoundId === greyhound);
  }

  /**
   * Reset
   */
  reset() {
    this.formGroup.reset();
    this.formGroup.controls.distance.setValue('0');
    this.formGroup.controls.grade.setValue('all');
    this.formGroup = this._greyhoundService.greyhoundFilterForm();
    this.greyhoundList = [];
    this.trainerData = [];
    this.dataSource = [];
    this.selectedGreyhound = null;
    this.selectedTrainer = null;
    sessionStorage.removeItem("greyhound");
  }

  /**
   * Get greyhound data
   */
  getGreyhoundData() {
    this.pagination.data = [];
    this.dataSource = [];
    let data = this.formGroup.getRawValue();
    let json = {
      filter: {
        track: data.track,
        trainerId: data.trainer,
        greyhoundId: data.greyhoundId,
        distance: data.distance,
        grade: data.grade,
        fromDate: data.fromDate,
        toDate: data.toDate
      },
      pagination: this.pagination
    }
    this.isLoading = true;
    this._greyhoundService.getGreyhoundData(json).then((response: any) => {
      this.pagination = response.body.data,
        this.dataSource = this.pagination.data
      this.dataSource.sort(function compare(a, b) {
        var dateA: any = new Date(a.date);
        var dateB: any = new Date(b.date);
        return dateB - dateA;
      });
      this.isLoading = false;
    })
  }

  /**
   * Show graph
   */
  showGraph() {
    let data = this.formGroup.getRawValue();
    sessionStorage.setItem("greyhound", JSON.stringify(data));
    this._route.navigate([`greyhound/graph`])
  }

  /**
   * Edit Comment
   * 
   * @param element 
   */
  editComment(element) {
    if (document.getElementById("show-comment-" + element.id))
      document.getElementById("show-comment-" + element.id).style.display = "none";

    if (document.getElementById("edit-comment-" + element.id))
      document.getElementById("edit-comment-" + element.id).style.display = "block";
  }

  /**
   * Save Comment
   * 
   * @param element 
   */
  saveComment(element: any) {
    if (document.getElementById("show-comment-" + element.id))
      document.getElementById("show-comment-" + element.id).style.display = "block";

    if (document.getElementById("edit-comment-" + element.id))
      document.getElementById("edit-comment-" + element.id).style.display = "none";

    let formData = new FormData();
    formData.append("id", element.id);
    formData.append("comment", element.comment);
    this._greyhoundService.updateComment(formData);
  }

  /**
   * Export
   */
  export() {
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    this._greyhoundService.exportExcel(data).then((response: any) => {
      this.greyhoundDataExport = response.body.data;
      this._greyhoundService.exportGreyhoundData(this.greyhoundDataExport);
      this.isLoading = false;
    });
  }

}
