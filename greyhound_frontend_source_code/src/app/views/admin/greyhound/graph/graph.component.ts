import { Component, OnInit, ViewChild } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { environment } from "environments/environment";
import { UtilityService } from "../../../../shared/services/utility.service";
import { TrainerService } from "../../trainer/trainer.service";
import { GreyhoundService } from "../greyhound.service";

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
  selector: "app-graph",
  templateUrl: "./graph.component.html",
  styleUrls: ["./graph.component.css"],
})
export class GraphComponent implements OnInit {
  formGroup!: FormGroup;
  isLoading: boolean = false;
  dataSource: any[] = [];
  trackData: any[] = [];
  trainerData: any[] = [];
  selectedGreyhound: any;
  greyhoundList: any[] = [];
  curruncy = environment.curruncySymbol;
  distanceData: any[] = [];
  gradeData: any[] = [];

  lineChartSteppedData: Array<any> = [
    {
      data: [],
      label: "",
      borderWidth: 0,
      fill: true,
      backgroundColor: ["navy", "black", "navy"],
      hoverBackgroundColor: ["darknavy", "darkblack", "darknavy"],
    },
  ];
  public lineChartLabels: Array<any> = [];

  public lineChartOptions: any = {
    responsive: true,
    maintainAspectRatio: true,
    legend: {
      display: true,
      position: "bottom",
    },
    scales: {
      xAxes: [
        {
          display: true,
          gridLines: {
            color: "rgba(0,0,0,0.02)",
            zeroLineColor: "rgba(0,0,0,0.02)",
          },
        },
      ],
      yAxes: [
        {
          display: true,
          gridLines: {
            color: "rgba(0,0,0,0.02)",
            zeroLineColor: "rgba(0,0,0,0.02)",
          },
          ticks: {
            beginAtZero: true,
            suggestedMax: 9,
          },
        },
      ],
    },
  };

  public lineChartColors: Array<any> = [];
  public lineChartLegend: boolean = true;
  public lineChartType: string = "line";

  selectedTrainer: any;

  @ViewChild("chart") chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  activeEntries: any[] = [];
  ratingYaces: any[] = [];
  prizeMoneyYaces: any[] = [];
  winYaces: any[] = [];

  constructor(
    private _activatedRoute: ActivatedRoute,
    private _greyhoundService: GreyhoundService,
    private _trainerService: TrainerService,
    public _utilityService: UtilityService,
    private _route: Router
  ) {
    // this.chartOptions = {
    //   series: [
    //     {
    //       name: "My-series",
    //       data: [10, 41, 35, 51, 49, 62, 69, 91, 148]
    //     },
    //     {
    //       name: "My-series",
    //       data: [10, 41, 35, 51, 49, 62, 69, 91, 148]
    //     }
    //   ],
    //   chart: {
    //     height: 350,
    //     type: "bar"
    //   },
    //   title: {
    //     text: "My First Angular Chart"
    //   },
    //   xaxis: {
    //     categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep"]
    //   }
    // };
  }

  async ngOnInit() {
    this.formGroup = this._greyhoundService.greyhoundFilterForm();
    await this.getAllTracks();
    await this.getAllDistance();
    await this.getAllGrade();
    if (sessionStorage.getItem("greyhound")) {
      this.formGroup = this._greyhoundService.greyhoundFilterForm(JSON.parse(sessionStorage.getItem("greyhound")));
      await this.onSelectionChangeTrack(this.formGroup.controls.track.value);
      await this.onSelectionChangeTrainer(this.formGroup.controls.trainer.value);
      this.onSelectionChangeGreyhound(this.formGroup.controls.greyhoundId.value);
    }
    this.getGreyhoundData();
  }

  async onSelectionChangeTrack(track: any) {
    let tracks = "";
    track.forEach((element) => {
      tracks += `tracks=${element}&`;
    });
    tracks = tracks.substring(0, tracks.length - 1);
    this.isLoading = true;
    await this._trainerService
      .getTrainerListByTracks(tracks)
      .then((response: any) => {
        this.trainerData = response.body.data;
        this.trainerData.sort(function (a, b) {
          if (a.name < b.name) { return -1; }
          // if(a.firstname > b.firstname) { return 1; }
          return 0;
        })
        this.isLoading = false;
      });
  }

  async onSelectionChangeTrainer(trainerId) {
    this.selectedTrainer = this.trainerData.find(item => item.id === trainerId);
    await this._greyhoundService
      .getGreyhoundByTrainer(trainerId)
      .then((response: any) => {
        this.greyhoundList = response.body.data;
        this.greyhoundList.sort(function (a, b) {
          if (a.name < b.name) { return -1; }
          // if(a.firstname > b.firstname) { return 1; }
          return 0;
        })
        this.isLoading = false;
      });
  }

  onSelectionChangeGreyhound(greyhound) {
    this.selectedGreyhound = this.greyhoundList.find(
      (item) => item.greyhoundId === parseInt(greyhound)
    );
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

  public wins: string = "Wins";
  public prize: string = "Prize Money in £";

  reset() {
    this.formGroup.reset();
    this.formGroup.controls.distance.setValue('0');
    this.formGroup.controls.grade.setValue('all');
    this.greyhoundList = [];
    this.trainerData = [];
    this.dataSource = [];
  }

  getGreyhoundData() {
    let data = this.formGroup.getRawValue();
    sessionStorage.setItem("greyhound", JSON.stringify(data));
    let filterData = {
      track: data.track,
      trainerId: data.trainer,
      greyhoundId: data.greyhoundId,
      distance: data.distance,
      grade: data.grade
    };
    this.isLoading = true;
    this._greyhoundService
      .getGreyhoundGraphData(filterData)
      .then((response: any) => {
        this.dataSource = response.body.data;
        let chartDataList: any[] = [];
        let data = Object.values(this.dataSource);
        this.ratingYaces = [];
        this.prizeMoneyYaces = [];
        let xData = Object.keys(this.dataSource);
        data.forEach((element, index) => {
          this.ratingYaces.push(parseFloat(element.rating));
          this.prizeMoneyYaces.push(parseFloat(element.prize));
          this.winYaces.push(element.win);
        });
        var randomColor =
          "#" + Math.floor(Math.random() * 16777215).toString(16);
        var randomColor1 =
          "#" + Math.floor(Math.random() * 16766215).toString(16);
        var randomColor2 =
          "#" + Math.floor(Math.random() * 16766215).toString(16);
        chartDataList.push({
          label: this.wins,
          data: this.ratingYaces,
          borderWidth: 0,
          fill: true,
          backgroundColor: [randomColor1],
          steppedLine: true,
        });
        chartDataList.push({
          label: this.prize,
          data: this.prizeMoneyYaces,
          borderWidth: 0,
          fill: true,
          backgroundColor: [randomColor],
          steppedLine: true,
        });
        chartDataList.push({
          label: this.prize,
          data: this.winYaces,
          borderWidth: 0,
          fill: true,
          backgroundColor: [randomColor2],
          steppedLine: true,
        });
        this.lineChartSteppedData = chartDataList;
        this.lineChartLabels = xData;

        this.chartOptions = {
          series: [
            {
              name: "Rating",
              data: this.ratingYaces
            },
            {
              name: "Prize",
              data: this.prizeMoneyYaces
            },
            {
              name: "Win",
              data: this.winYaces
            }
          ],
          chart: {
            height: 350,
            type: "bar",
            events: {
              legendClick: function (chartContext, seriesIndex, config) {
                console.log(config);
              }
            }
          },
          title: {
            text: ""
          },
          xaxis: {
            categories: this.lineChartLabels
          }
        };
        this.isLoading = false;
      });
  }

  public legendLabelActivate(item: any): void {
    this.activeEntries = [item];
  }

  public legendLabelDeactivate(item: any): void {
    this.activeEntries = [];
  }

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
    });
  }

  backBtn() {
    this._route.navigateByUrl('/greyhound');
  }

}
