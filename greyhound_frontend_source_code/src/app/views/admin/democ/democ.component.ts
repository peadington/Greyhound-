import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-democ',
  templateUrl: './democ.component.html',
  styleUrls: ['./democ.component.scss']
})
export class DemocComponent implements OnInit {
  orderData: any[] = ["BY Rank", "By STmHcp", "By WinTime", "By CalcTm"];
  foods= [
    {value: 'steak-0', viewValue: 'Steak'},
    {value: 'pizza-1', viewValue: 'Pizza'},
    {value: 'tacos-2', viewValue: 'Tacos'},
  ];

  constructor() { }

  ngOnInit(): void {
  }

}
