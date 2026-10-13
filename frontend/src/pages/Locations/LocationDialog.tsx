import { useEffect, useState } from 'react';
import type { FormEvent } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    Alert,
} from '@mui/material';
import { ApiError } from '../../api/client';
import { createLocation, updateLocation } from '../../api/locations';
import type { Location } from '../../types/location';
import styles from './LocationDialog.module.css';

interface FormState {
    x: string;
    y: string;
    z: string;
    name: string;
}

const EMPTY_FORM: FormState = { x: '', y: '', z: '', name: '' };

function formFromLocation(location: Location): FormState {
    return {
        x: String(location.x),
        y: String(location.y),
        z: String(location.z),
        name: location.name ?? '',
    };
}

interface LocationDialogProps {
    open: boolean;
    location: Location | null;
    onClose: () => void;
    onSaved: (saved: Location) => void;
}

function LocationDialog({
    open,
    location,
    onClose,
    onSaved,
}: LocationDialogProps) {
    const [form, setForm] = useState<FormState>(EMPTY_FORM);
    const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
    const [submitError, setSubmitError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        if (!open) return;
        setForm(location ? formFromLocation(location) : EMPTY_FORM);
        setFieldErrors({});
        setSubmitError(null);
    }, [open, location]);

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
            x: Number(form.x),
            y: Number(form.y),
            z: Number(form.z),
            name: form.name.trim() === '' ? null : form.name,
        };

        const request = location
            ? updateLocation(location.id, dto)
            : createLocation(dto);

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
        <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
            <DialogTitle>
                {location
                    ? `Редактировать Location #${location.id}`
                    : 'Новая Location'}
            </DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent>
                    <div className={styles.form}>
                        {submitError && (
                            <Alert severity="error">{submitError}</Alert>
                        )}

                        <div className={styles.row}>
                            <TextField
                                className={styles.field}
                                label="x"
                                value={form.x}
                                onChange={(e) => setField('x', e.target.value)}
                                error={Boolean(fieldError('x'))}
                                helperText={fieldError('x')}
                                required
                            />
                            <TextField
                                className={styles.field}
                                label="y"
                                value={form.y}
                                onChange={(e) => setField('y', e.target.value)}
                                error={Boolean(fieldError('y'))}
                                helperText={fieldError('y')}
                                required
                            />
                            <TextField
                                className={styles.field}
                                label="z"
                                value={form.z}
                                onChange={(e) => setField('z', e.target.value)}
                                error={Boolean(fieldError('z'))}
                                helperText={fieldError('z')}
                                required
                            />
                        </div>

                        <TextField
                            label="Имя (необязательно)"
                            value={form.name}
                            onChange={(e) => setField('name', e.target.value)}
                            error={Boolean(fieldError('name'))}
                            helperText={fieldError('name')}
                            fullWidth
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

export default LocationDialog;
