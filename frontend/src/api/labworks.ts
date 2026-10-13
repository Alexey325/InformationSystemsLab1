import { apiFetch } from './client';
import type { LabWork, LabWorkRequestDto, NameCount } from '../types/labwork';
import type { Page } from '../types/api';

export interface FetchLabWorksParams {
    page: number;
    size: number;
    sortField?: string | null;
    sortDir?: 'asc' | 'desc';
    name?: string;
    description?: string;
}

export function fetchLabWorks(
    params: FetchLabWorksParams,
): Promise<Page<LabWork>> {
    const { page, size, sortField, sortDir, name, description } = params;
    const query = new URLSearchParams();
    query.set('page', String(page));
    query.set('size', String(size));
    if (sortField) query.set('sort', `${sortField},${sortDir ?? 'asc'}`);
    if (name) query.set('name', name);
    if (description) query.set('description', description);

    return apiFetch<Page<LabWork>>(`/labworks?${query.toString()}`);
}

export function createLabWork(dto: LabWorkRequestDto): Promise<LabWork> {
    return apiFetch<LabWork>('/labworks', {
        method: 'POST',
        body: JSON.stringify(dto),
    });
}

export function updateLabWork(
    id: number,
    dto: LabWorkRequestDto,
): Promise<LabWork> {
    return apiFetch<LabWork>(`/labworks/${id}`, {
        method: 'PUT',
        body: JSON.stringify(dto),
    });
}

export function deleteLabWork(id: number): Promise<null> {
    return apiFetch<null>(`/labworks/${id}`, {
        method: 'DELETE',
    });
}

export function fetchMaxTunedInWork(): Promise<LabWork> {
    return apiFetch<LabWork>('/labworks/max-tuned-in-work');
}

export function fetchGroupedByName(): Promise<NameCount[]> {
    return apiFetch<NameCount[]>('/labworks/grouped-by-name');
}

export function fetchByMinimalPointGreaterThan(
    minimalPoint: number,
): Promise<LabWork[]> {
    return apiFetch<LabWork[]>(
        `/labworks/minimal-point-greater-than?minimalPoint=${minimalPoint}`,
    );
}

export function addHardestToDiscipline(
    disciplineId: number,
): Promise<LabWork[]> {
    return apiFetch<LabWork[]>(
        `/labworks/add-hardest-to-discipline?disciplineId=${disciplineId}`,
        { method: 'POST' },
    );
}
