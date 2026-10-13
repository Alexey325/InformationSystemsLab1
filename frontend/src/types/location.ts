import type { PersonRef } from './labwork';

export interface LocationRef {
    id: number;
    name: string | null;
}

export interface Location {
    id: number;
    x: number;
    y: number;
    z: number;
    name: string | null;
    people: PersonRef[];
}

export interface LocationRequestDto {
    x: number;
    y: number;
    z: number;
    name: string | null;
}
