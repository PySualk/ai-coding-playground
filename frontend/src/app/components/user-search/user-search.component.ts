import { Component, ChangeDetectionStrategy, output, signal } from '@angular/core';
import { IonSearchbar, IonSegment, IonSegmentButton, IonLabel } from '@ionic/angular/standalone';
import { debounceTime } from 'rxjs/operators';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-user-search',
  imports: [IonSearchbar, IonSegment, IonSegmentButton, IonLabel],
  templateUrl: './user-search.component.html',
  styleUrl: './user-search.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserSearchComponent {
  searchChange = output<string>();
  filterChange = output<'all' | 'active' | 'inactive'>();

  selectedFilter = signal<'all' | 'active' | 'inactive'>('all');
  private searchSubject = new Subject<string>();

  constructor() {
    // Debounce search input
    this.searchSubject.pipe(debounceTime(300)).subscribe(query => {
      this.searchChange.emit(query);
    });
  }

  onSearchInput(event: CustomEvent) {
    const query = (event.detail.value || '').trim();
    this.searchSubject.next(query);
  }

  onFilterChange(event: CustomEvent) {
    const filter = event.detail.value as 'all' | 'active' | 'inactive';
    this.selectedFilter.set(filter);
    this.filterChange.emit(filter);
  }
}
