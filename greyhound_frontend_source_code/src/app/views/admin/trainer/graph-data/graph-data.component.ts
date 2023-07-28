import { Component, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { GreyhoundService } from '../../greyhound/greyhound.service';
import { TrainerService } from '../trainer.service';

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexTitleSubtitle
} from "ng-apexcharts";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  title: ApexTitleSubtitle;
};

@Component({
  selector: 'app-graph-data',
  templateUrl: './graph-data.component.html',
  styleUrls: ['./graph-data.component.scss']
})
export class GraphTrainerDataComponent {
  formGroup!: FormGroup;
  isLoading: boolean = false;
  greyhoundNames: any[] = [];
  typeData: any[] = ['Cumlative', 'Distances', 'Grades']
  selectedNames: any = []
  shftesVar: any[] = [];
  trainer: any;
  track: any;
  minDate: Date = new Date();
  trainerData: any[] = [];
  trackList: any[] = [];
  gradeData: any;
  dataSource: any;
  distanceData: any;
  dataFilter: any
  selectedTrainer: any;

  @ViewChild("chart") chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  winData: any[] = [];
  prizeMoneyData: any[] = [];
  ratingData: any[] = [];

  constructor(
    private _trainerService: TrainerService,
    private _activatedRoute: ActivatedRoute,
    private _greyhoundService: GreyhoundService,
    private _route: Router
  ) {
    this.formGroup = this._trainerService.createFilterForm();
  }

  async ngOnInit() {
    await this.getAllTracks();
    await this.getAllGrade();
    await this.getAllDistance();
    if (sessionStorage.getItem("trainer")) {
      this.formGroup = this._trainerService.trainerFilterForm(JSON.parse(sessionStorage.getItem("trainer")));
      await this.onSelectionChangeTrack(this.formGroup.controls.track.value);
      await this.onSelectionChangeTrainer(this.formGroup.controls.trainerId.value);
      this.trainerGraph()
    }

    this.trainerGraph();
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

  lineChartSteppedData: Array<any> = [];
  public lineChartLabels: Array<any> = [];
  /*
  * Full width Chart Options
  */
  public wins: string = "Wins";
  public prize: string = "Prize Money in £";
  public rating: string = "Rating";

  reset() {
    this.formGroup.reset();
    this.formGroup.controls.distance.setValue('0');
    this.formGroup.controls.grade.setValue('all');
    this.trainerData = [];
    this.dataSource = [];
  }

  async trainerGraph() {
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    sessionStorage.setItem("trainer", JSON.stringify(data));
    this._trainerService.getChartData(data).then((response: any) => {
      this.dataSource = response.body.data;
      let chartDataList: any[] = [];
      let data = Object.keys(this.dataSource)
      this.winData = [];
      this.prizeMoneyData = [];
      this.ratingData = [];
      data.forEach(element => {
        this.winData.push(this.dataSource[element].win);
        this.prizeMoneyData.push(this.dataSource[element].prize);
        this.ratingData.push(this.dataSource[element].rating);
      });
      var randomColor = "#" + Math.floor(Math.random() * 16777215).toString(16);
      var randomColor1 = "#" + Math.floor(Math.random() * 16766215).toString(16);
      var randomColor2 = "#" + Math.floor(Math.random() * 16766215).toString(16);
      chartDataList.push(
        {
          label: this.wins,
          data: this.winData,
          borderWidth: 0,
          fill: true,
          backgroundColor: [randomColor1],
          steppedLine: false
        }
      );
      chartDataList.push(
        {
          label: this.prize,
          data: this.prizeMoneyData,
          borderWidth: 0,
          fill: true,
          backgroundColor: [randomColor],
          steppedLine: true
        }
      );
      chartDataList.push(
        {
          label: this.rating,
          data: this.ratingData,
          borderWidth: 0,
          fill: true,
          backgroundColor: [randomColor2],
          steppedLine: true
        }
      );
      this.lineChartSteppedData = chartDataList;
      this.lineChartLabels = Object.keys(this.dataSource);

      this.chartOptions = {
        series: [
          {
            name: "Rating",
            data: this.ratingData
          },
          {
            name: "Prize",
            data: this.prizeMoneyData
          },
          {
            name: "Win",
            data: this.winData
          }
        ],
        chart: {
          height: 350,
          type: "bar"
        },
        title: {
          text: ""
        },
        xaxis: {
          categories: this.lineChartLabels
        }
      };
      this.isLoading = false;
    })
  }

  public lineChartOptions: any = {
    responsive: true,
    maintainAspectRatio: false,
    legend: {
      display: false,
      position: 'bottom'
    },
    scales: {
      xAxes: [{
        display: false,
        gridLines: {
          color: 'rgba(0,0,0,0.02)',
          zeroLineColor: 'rgba(0,0,0,0.02)'
        }
      }],
      yAxes: [{
        display: false,
        gridLines: {
          color: 'rgba(0,0,0,0.02)',
          zeroLineColor: 'rgba(0,0,0,0.02)'
        },
        ticks: {
          beginAtZero: true,
          suggestedMax: 9,
        }
      }]
    }
  };

  public lineChartColors: Array<any> = [];
  public lineChartLegend: boolean = true;
  public lineChartType: string = 'line';

  // setGraph(chartKind) {
  //   let xacesData = Object.keys(this.graphData);
  //   let dataList: any[] = [];
  //   var randomColor7 = "#" + Math.floor(Math.random() * 16777215).toString(16);
  //   var randomColor30 = "#" + Math.floor(Math.random() * 16777215).toString(16);
  //   var randomColor60 = "#" + Math.floor(Math.random() * 16777215).toString(16);
  //   var randomColor90 = "#" + Math.floor(Math.random() * 16777215).toString(16);
  //   var randomColor120 = "#" + Math.floor(Math.random() * 16777215).toString(16);
  //   var randomColor180 = "#" + Math.floor(Math.random() * 16777215).toString(16);
  //   var randomColor365 = "#" + Math.floor(Math.random() * 16777215).toString(16);
  //   xacesData.forEach(element => {
  //     let valueList: any[] = [];
  //     if (chartKind === 'prize') {
  //       valueList.push(this.graphData['prizeMoney']);
  //     } else {
  //       valueList.push(this.graphData['position1']);
  //       valueList.push(this.graphData['position2']);
  //       valueList.push(this.graphData['position3']);
  //       valueList.push(this.graphData['position4']);
  //       valueList.push(this.graphData['position5']);
  //       valueList.push(this.graphData['position6']);

  //       // valueList.push(5);
  //       // valueList.push(6);
  //       // valueList.push(1);
  //       // valueList.push(4);
  //       // valueList.push(2);
  //       // valueList.push(7);
  //     }

  //     dataList.push(
  //       {
  //         data: valueList,
  //         borderWidth: 0,
  //         fill: true,
  //         backgroundColor: [randomColor7, randomColor30, randomColor60, randomColor90, randomColor120, randomColor180, randomColor365],
  //         hoverBackgroundColor: ["darkgrey", "darkblack"]
  //       }
  //     );
  //   });
  //   this.lineChartSteppedData = dataList;
  //   this.lineChartLabels = xacesData;
  // }
  async getAllDistance() {
    await this._trainerService.getDistanceData().then((response: any) => {
      this.distanceData = response.body.data
    })
  }
  async getAllGrade() {
    await this._trainerService.getGradeData().then((response: any) => {
      this.gradeData = response.body.data
    })
  }
  onSelectionChangeType(value) {
    this.formGroup.controls.grade.setValue(null);
    this.formGroup.controls.distance.setValue(null);
  }
  backBtn() {
    this._route.navigateByUrl('/trainer');
  }

  async onSelectionChangeTrack(track: any) {
    this.isLoading = true;
    await this._trainerService.getTrainerByTrack(track).then((response: any) => {
      this.trainerData = response.body.data;
      this.trainerData.sort(function (a, b) {
        if (a.name < b.name) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    })
  }

  async onSelectionChangeTrainer(trainerId) {
    this.selectedTrainer = this.trainerData.find(item => item.id === trainerId);
  }
}
