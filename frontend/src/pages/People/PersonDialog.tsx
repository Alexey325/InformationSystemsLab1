import { useEffect, useState } from 'react';
import type { FormEvent } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    MenuItem,
    Alert,
} from '@mui/material';
import { ApiError } from '../../api/client';
import { createPerson, updatePerson } from '../../api/people';
import { fetchLocations } from '../../api/locations';
import type { Color, Country, Person } from '../../types/person';
import { COLORS, COUNTRIES } from '../../types/person';
import type { LocationRef } from '../../types/location';
import styles from './PersonDialog.module.css';

interface FormState {
    name: string;
    eyeColor: Color | '';
    hairColor: Color | '';
    locationId: string;
    passportID: string;
    nationality: Country | '';
}

const EMPTY_FORM: FormState = {
    name: '',
    eyeColor: '',
    hairColor: '',
    locationId: '',
    passportID: '',
    nationality: '',
};

function formFromPerson(person: Person): FormState {
    return {
        name: person.name,
        eyeColor: person.eyeColor ?? '',
        hairColor: person.hairColor ?? '',
        locationId: String(person.location.id),
        passportID: person.passportID,
        nationality: person.nationality,
    };
}

interface PersonDialogProps {
    open: boolean;
    person: Person | null;
    onClose: () => void;
    onSaved: (saved: Person) => void;
}

function PersonDialog({ open, person, onClose, onSaved }: PersonDialogProps) {
    const [form, setForm] = useState<FormState>(EMPTY_FORM);
    const [locations, setLocations] = useState<LocationRef[]>([]);
    const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
    const [submitError, setSubmitError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        if (!open) return;
        setForm(person ? formFromPerson(person) : EMPTY_FORM);
        setFieldErrors({});
        setSubmitError(null);
    }, [open, person]);

    useEffect(() => {
        if (!open) return;
        fetchLocations()
            .then(setLocations)
            .catch(() => setLocations([]));
    }, [open]);

    function setField<K extends keyof FormState>(key: K, value: FormState[K]) {
        setForm((prev) => ({ ...prev, [key]: value }));
    }

    function fieldError(name: string): string | undefined {
        return fieldErrors[name];
    }

    function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setSubmitError(null);
        setFieldErrors({});
        setSubmitting(true);

        const dto = {
            name: form.name,
            eyeColor: form.eyeColor === '' ? null : form.eyeColor,
            hairColor: form.hairColor === '' ? null : form.hairColor,
            locationId: Number(form.locationId),
            passportID: form.passportID,
            nationality: form.nationality as Country,
        };

        const request = person
            ? updatePerson(person.id, dto)
            : createPerson(dto);

        request
            .then((saved) => {
                onSaved(saved);
                onClose();
            })
            .catch((err: unknown) => {
                if (err instanceof ApiError && err.status === 400) {
                    const errors = err.problemDetail?.errors ?? [];
                    const map: Record<string, string> = {};
                    errors.forEach((fe) => {
                        map[fe.field] = fe.message;
                    });
                    setFieldErrors(map);
                } else if (err instanceof ApiError) {
                    setSubmitError(err.problemDetail?.detail ?? err.message);
                } else {
                    setSubmitError('Неизвестная ошибка');
                }
            })
            .finally(() => setSubmitting(false));
    }

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>
                {person ? `Редактировать ${person.name}` : 'Новый Person'}
            </DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent>
                    <div className={styles.form}>
                        {submitError && (
                            <Alert severity="error">{submitError}</Alert>
                        )}

                        <TextField
                            label="Имя"
                            value={form.name}
                            onChange={(e) => setField('name', e.target.value)}
                            error={Boolean(fieldError('name'))}
                            helperText={fieldError('name')}
                            fullWidth
                            required
                        />

                        <div className={styles.row}>
                            <TextField
                                className={styles.field}
                                label="Eye color"
                                select
                                value={form.eyeColor}
                                onChange={(e) =>
                                    setField(
                                        'eyeColor',
                                        e.target.value as Color | '',
                                    )
                                }
                                error={Boolean(fieldError('eyeColor'))}
                                helperText={fieldError('eyeColor')}
                            >
                                <MenuItem value="">—</MenuItem>
                                {COLORS.map((c) => (
                                    <MenuItem key={c} value={c}>
                                        {c}
                                    </MenuItem>
                                ))}
                            </TextField>

                            <TextField
                                className={styles.field}
                                label="Hair color"
                                select
                                value={form.hairColor}
                                onChange={(e) =>
                                    setField(
                                        'hairColor',
                                        e.target.value as Color | '',
                                    )
                                }
                                error={Boolean(fieldError('hairColor'))}
                                helperText={fieldError('hairColor')}
                            >
                                <MenuItem value="">—</MenuItem>
                                {COLORS.map((c) => (
                                    <MenuItem key={c} value={c}>
                                        {c}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </div>

                        <div className={styles.row}>
                            <TextField
                                className={styles.field}
                                label="Location"
                                select
                                value={form.locationId}
                                onChange={(e) =>
                                    setField('locationId', e.target.value)
                                }
                                error={Boolean(fieldError('locationId'))}
                                helperText={fieldError('locationId')}
                                required
                            >
                                {locations.map((l) => (
                                    <MenuItem key={l.id} value={String(l.id)}>
                                        {l.name ?? `Location #${l.id}`}
                                    </MenuItem>
                                ))}
                            </TextField>

                            <TextField
                                className={styles.field}
                                label="Nationality"
                                select
                                value={form.nationality}
                                onChange={(e) =>
                                    setField(
                                        'nationality',
                                        e.target.value as Country | '',
                                    )
                                }
                                error={Boolean(fieldError('nationality'))}
                                helperText={fieldError('nationality')}
                                required
                            >
                                {COUNTRIES.map((c) => (
                                    <MenuItem key={c} value={c}>
                                        {c}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </div>

                        <TextField
                            label="Passport ID"
                            value={form.passportID}
                            onChange={(e) =>
                                setField('passportID', e.target.value)
                            }
                            error={Boolean(fieldError('passportID'))}
                            helperText={
                                fieldError('passportID') ??
                                'Максимум 24 символа'
                            }
                            slotProps={{ htmlInput: { maxLength: 24 } }}
                            fullWidth
                            required
                        />
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose} disabled={submitting}>
                        Отмена
                    </Button>
                    <Button
                        type="submit"
                        variant="contained"
                        disabled={submitting}
                    >
                        Сохранить
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
}

export default PersonDialog;
