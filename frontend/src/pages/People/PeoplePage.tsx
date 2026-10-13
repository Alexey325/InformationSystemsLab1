import { useEffect, useState } from 'react';
import {
    Paper,
    Typography,
    Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    IconButton,
    Alert,
    CircularProgress,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { deletePerson, fetchPeople } from '../../api/people';
import type { Person } from '../../types/person';
import ConfirmDialog from '../../components/ConfirmDialog';
import PersonDialog from './PersonDialog';
import styles from './PeoplePage.module.css';

function PeoplePage() {
    const [people, setPeople] = useState<Person[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [refreshKey, setRefreshKey] = useState(0);

    const [dialogOpen, setDialogOpen] = useState(false);
    const [editingPerson, setEditingPerson] = useState<Person | null>(null);
    const [deleteTarget, setDeleteTarget] = useState<Person | null>(null);

    useEffect(() => {
        setLoading(true);
        setError(null);
        fetchPeople()
            .then(setPeople)
            .catch((err: Error) => setError(err.message))
            .finally(() => setLoading(false));
    }, [refreshKey]);

    function handleAdd() {
        setEditingPerson(null);
        setDialogOpen(true);
    }

    function handleEdit(p: Person) {
        setEditingPerson(p);
        setDialogOpen(true);
    }

    function handleSaved() {
        setRefreshKey((k) => k + 1);
    }

    function handleDeleteClick(p: Person) {
        setDeleteTarget(p);
    }

    function handleConfirmDelete() {
        if (!deleteTarget) return;
        deletePerson(deleteTarget.id)
            .then(() => setRefreshKey((k) => k + 1))
            .catch((err: Error) => setError(err.message))
            .finally(() => setDeleteTarget(null));
    }

    return (
        <div className={styles.page}>
            <Paper className={styles.headerCard}>
                <Typography variant="h5" className={styles.title}>
                    Person
                </Typography>
                <span className={styles.totalBadge}>
                    Всего: {people.length}
                </span>
            </Paper>

            <Paper className={styles.actionsCard}>
                <Button variant="contained" onClick={handleAdd}>
                    + Добавить Person
                </Button>
            </Paper>

            {error && <Alert severity="error">{error}</Alert>}

            <Paper className={styles.tableCard}>
                <TableContainer className={styles.tableScroll}>
                    <Table size="small" className={styles.table}>
                        <TableHead>
                            <TableRow>
                                <TableCell className={styles.headCell}>
                                    ID
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Name
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Eye color
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Hair color
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Location
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Passport ID
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Nationality
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    LabWorks
                                </TableCell>
                                <TableCell
                                    className={styles.headCell}
                                    align="right"
                                >
                                    Действия
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {people.map((p) => (
                                <TableRow key={p.id} hover>
                                    <TableCell>{p.id}</TableCell>
                                    <TableCell>{p.name}</TableCell>
                                    <TableCell>{p.eyeColor ?? '—'}</TableCell>
                                    <TableCell>{p.hairColor ?? '—'}</TableCell>
                                    <TableCell>
                                        {p.location.name ?? `#${p.location.id}`}
                                    </TableCell>
                                    <TableCell>{p.passportID}</TableCell>
                                    <TableCell>{p.nationality}</TableCell>
                                    <TableCell className={styles.ellipsisCell}>
                                        {p.labWorks
                                            .map((lw) => lw.name)
                                            .join(', ') || '—'}
                                    </TableCell>
                                    <TableCell align="right">
                                        <IconButton
                                            size="small"
                                            onClick={() => handleEdit(p)}
                                            aria-label={`Редактировать ${p.name}`}
                                        >
                                            <EditIcon fontSize="small" />
                                        </IconButton>
                                        <IconButton
                                            size="small"
                                            onClick={() => handleDeleteClick(p)}
                                            aria-label={`Удалить ${p.name}`}
                                        >
                                            <DeleteIcon fontSize="small" />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                            {!loading && people.length === 0 && (
                                <TableRow>
                                    <TableCell
                                        colSpan={9}
                                        align="center"
                                        className={styles.emptyCell}
                                    >
                                        Ничего не найдено
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>

                {loading && (
                    <div className={styles.loadingRow}>
                        <CircularProgress size={24} />
                    </div>
                )}
            </Paper>

            <PersonDialog
                open={dialogOpen}
                person={editingPerson}
                onClose={() => setDialogOpen(false)}
                onSaved={handleSaved}
            />

            <ConfirmDialog
                open={deleteTarget !== null}
                title="Удалить Person?"
                message={`Удалить "${deleteTarget?.name}"? Это действие необратимо.`}
                onConfirm={handleConfirmDelete}
                onCancel={() => setDeleteTarget(null)}
            />
        </div>
    );
}

export default PeoplePage;
