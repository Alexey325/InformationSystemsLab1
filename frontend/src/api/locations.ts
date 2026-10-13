import { apiFetch } from './client';
import type { Location, LocationRequestDto } from '../types/location';

export function fetchLocations(): Promise<Location[]> {
    return apiFetch<Location[]>('/locations');
}

export function createLocation(dto: LocationRequestDto): Promise<Location> {
    return apiFetch<Location>('/locations', {
        method: 'POST',
        body: JSON.stringify(dto),
    });
}

export function updateLocation(
    id: number,
    dto: LocationRequestDto,
): Promise<Location> {
    return apiFetch<Location>(`/locations/${id}`, {
        method: 'PUT',
        body: JSON.stringify(dto),
    });
}

export function deleteLocation(id: number): Promise<null> {
    return apiFetch<null>(`/locations/${id}`, {
        method: 'DELETE',
    });
}
