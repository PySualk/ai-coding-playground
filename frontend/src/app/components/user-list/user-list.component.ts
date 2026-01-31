import { Component, ChangeDetectionStrategy, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import {
  IonList,
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonCardContent,
  IonChip,
  IonButton,
  IonIcon,
  IonSkeletonText,
  IonLabel
} from '@ionic/angular/standalone';
import { User } from '../../models/user.model';
import { addIcons } from 'ionicons';
import { createOutline, trashOutline } from 'ionicons/icons';

@Component({
  selector: 'app-user-list',
  imports: [
    DatePipe,
    IonList,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonChip,
    IonButton,
    IonIcon,
    IonSkeletonText,
    IonLabel
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserListComponent {
  users = input.required<User[]>();
  loading = input<boolean>(false);

  userEdit = output<User>();
  userDelete = output<User>();

  constructor() {
    addIcons({ createOutline, trashOutline });
  }

  onEdit(user: User) {
    this.userEdit.emit(user);
  }

  onDelete(user: User) {
    this.userDelete.emit(user);
  }
}
