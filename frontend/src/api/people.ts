import { apiFetch } from './client';
import type { Person, PersonRequestDto } from '../types/person';

export function fetchPeople(): Promise<Person[]> {
    return apiFetch<Person[]>('/persons');
}

export function createPerson(dto: PersonRequestDto): Promise<Person> {
    return apiFetch<Person>('/persons', {
        method: 'POST',
        body: JSON.stringify(dto),
    });
}

export function updatePerson(
    id: number,
    dto: PersonRequestDto,
): Promise<Person> {
    return apiFetch<Person>(`/persons/${id}`, {
        method: 'PUT',
        body: JSON.stringify(dto),
    });
}

export function deletePerson(id: number): Promise<null> {
    return apiFetch<null>(`/persons/${id}`, {
        method: 'DELETE',
    });
}
