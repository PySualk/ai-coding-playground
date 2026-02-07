import { Component, ChangeDetectionStrategy, input, output } from '@angular/core';
import {
  IonList,
  IonItem,
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
    IonList,
    IonItem,
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

  /**
   * Generate user initials from first and last name
   */
  getInitials(user: User): string {
    const firstInitial = user.firstName?.charAt(0).toUpperCase() || '';
    const lastInitial = user.lastName?.charAt(0).toUpperCase() || '';
    return `${firstInitial}${lastInitial}`;
  }

  /**
   * Convert timestamp to relative time (e.g., "2h ago", "3d ago")
   */
  getRelativeTime(timestamp: string): string {
    const now = new Date();
    const past = new Date(timestamp);
    const diffMs = now.getTime() - past.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'Just now';
    if (diffMins < 60) return `${diffMins}m ago`;
    if (diffHours < 24) return `${diffHours}h ago`;
    if (diffDays < 7) return `${diffDays}d ago`;
    if (diffDays < 30) return `${Math.floor(diffDays / 7)}w ago`;
    return `${Math.floor(diffDays / 30)}mo ago`;
  }

  onEdit(user: User) {
    this.userEdit.emit(user);
  }

  onDelete(user: User) {
    this.userDelete.emit(user);
  }
}
