import { apiFetch } from './client';
import type { Discipline, DisciplineRequestDto } from '../types/discipline';

export function fetchDisciplines(): Promise<Discipline[]> {
    return apiFetch<Discipline[]>('/disciplines');
}

export function createDiscipline(
    dto: DisciplineRequestDto,
): Promise<Discipline> {
    return apiFetch<Discipline>('/disciplines', {
        method: 'POST',
        body: JSON.stringify(dto),
    });
}

export function updateDiscipline(
    id: number,
    dto: DisciplineRequestDto,
): Promise<Discipline> {
    return apiFetch<Discipline>(`/disciplines/${id}`, {
        method: 'PUT',
        body: JSON.stringify(dto),
    });
}

export function deleteDiscipline(id: number): Promise<null> {
    return apiFetch<null>(`/disciplines/${id}`, {
        method: 'DELETE',
    });
}
