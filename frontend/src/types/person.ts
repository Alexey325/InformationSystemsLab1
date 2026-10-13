import type { LabWorkRef } from './discipline';
import type { LocationRef } from './location';

export type Color = 'GREEN' | 'BLACK' | 'BROWN';
export type Country = 'RUSSIA' | 'GERMANY' | 'VATICAN';

export const COLORS: Color[] = ['GREEN', 'BLACK', 'BROWN'];
export const COUNTRIES: Country[] = ['RUSSIA', 'GERMANY', 'VATICAN'];

export interface Person {
    id: number;
    name: string;
    eyeColor: Color | null;
    hairColor: Color | null;
    location: LocationRef;
    passportID: string;
    nationality: Country;
    labWorks: LabWorkRef[];
}

export interface PersonRequestDto {
    name: string;
    eyeColor: Color | null;
    hairColor: Color | null;
    locationId: number;
    passportID: string;
    nationality: Country;
}
