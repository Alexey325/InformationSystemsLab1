export type Difficulty =
    'NORMAL' | 'HARD' | 'VERY_HARD' | 'INSANE' | 'HOPELESS';

export interface Coordinates {
    x: number;
    y: number;
}

export interface DisciplineRef {
    id: number;
    name: string;
}

export interface PersonRef {
    id: number;
    name: string;
}

export interface LabWork {
    id: number;
    name: string;
    coordinates: Coordinates;
    creationDate: string;
    description: string;
    difficulty: Difficulty | null;
    discipline: DisciplineRef;
    minimalPoint: number;
    personalQualitiesMaximum: number;
    tunedInWork: number;
    authors: PersonRef[];
}

export const DIFFICULTIES: Difficulty[] = [
    'NORMAL',
    'HARD',
    'VERY_HARD',
    'INSANE',
    'HOPELESS',
];

export interface NameCount {
    name: string;
    count: number;
}

export interface LabWorkRequestDto {
    name: string;
    coordinates: Coordinates;
    description: string;
    difficulty: Difficulty | null;
    minimalPoint: number;
    personalQualitiesMaximum: number;
    tunedInWork: number;
    disciplineId: number;
    authorsIds?: number[];
}
