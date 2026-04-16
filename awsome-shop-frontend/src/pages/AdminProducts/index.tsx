import { useEffect, useState, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Chip from '@mui/material/Chip';
import Switch from '@mui/material/Switch';
import IconButton from '@mui/material/IconButton';
import Pagination from '@mui/material/Pagination';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import SearchIcon from '@mui/icons-material/Search';
import { productService } from '../../services/productService';
import { categoryService } from '../../services/categoryService';

export default function AdminProducts() {
  const { t } = useTranslation();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [products, setProducts] = useState<any[]>([]);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [categories, setCategories] = useState<any[]>([]);
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [search, setSearch] = useState('');
  const [catFilter, setCatFilter] = useState<string>('');
  const [dialogOpen, setDialogOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [editItem, setEditItem] = useState<any>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);
  const [form, setForm] = useState({ name: '', description: '', categoryId: '', pointsPrice: '', stock: '', imageUrl: '' });
  const size = 10;

  useEffect(() => {
    categoryService.listCategories()
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .then((res: any) => setCategories(res?.data ?? res ?? []))
      .catch(() => {});
  }, []);

  const fetchProducts = useCallback(async () => {
    try {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const res: any = await productService.listProducts({
        page, size, name: search || undefined,
        category: catFilter || undefined,
      });
      const data = res?.data ?? res;
      setProducts(data?.records ?? data?.list ?? []);
      setTotal(data?.total ?? 0);
    } catch { setProducts([]); }
  }, [page, search, catFilter]);

  useEffect(() => { fetchProducts(); }, [fetchProducts]);

  const openCreate = () => {
    setEditItem(null);
    setForm({ name: '', description: '', categoryId: '', pointsPrice: '', stock: '', imageUrl: '' });
    setDialogOpen(true);
  };

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const openEdit = (item: any) => {
    setEditItem(item);
    setForm({ name: item.name, description: item.description || '', categoryId: String(item.categoryId || ''), pointsPrice: String(item.pointsPrice || ''), stock: String(item.stock || ''), imageUrl: item.imageUrl || '' });
    setDialogOpen(true);
  };

  const handleSave = async () => {
    const payload = { ...form, categoryId: Number(form.categoryId), pointsPrice: Number(form.pointsPrice), stock: Number(form.stock) };
    try {
      if (editItem) {
        await productService.updateProduct({ id: editItem.id, ...payload });
      } else {
        await productService.createProduct(payload);
      }
      setDialogOpen(false);
      fetchProducts();
    } catch { /* ignore */ }
  };

  const handleToggle = async (id: number) => {
    try { await productService.toggleStatus(id); fetchProducts(); } catch { /* ignore */ }
  };

  const handleDelete = async () => {
    if (deleteId == null) return;
    try { await productService.deleteProduct(deleteId); setDeleteOpen(false); fetchProducts(); } catch { /* ignore */ }
  };

  return (
    <Box sx={{ p: '32px', display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography sx={{ fontSize: 24, fontWeight: 700 }}>{t('adminProducts.title')}</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={openCreate} data-testid="add-product-btn" sx={{ textTransform: 'none' }}>{t('adminProducts.add')}</Button>
      </Box>

      <Box sx={{ display: 'flex', gap: 2 }}>
        <TextField size="small" placeholder={t('adminProducts.searchPlaceholder')} value={search} onChange={(e) => { setSearch(e.target.value); setPage(1); }}
          slotProps={{ input: { startAdornment: <SearchIcon sx={{ fontSize: 18, color: 'text.secondary', mr: 1 }} /> } }} sx={{ width: 260 }} data-testid="product-search" />
        <FormControl size="small" sx={{ minWidth: 160 }}>
          <InputLabel>{t('adminProducts.category')}</InputLabel>
          <Select value={catFilter} label={t('adminProducts.category')} onChange={(e) => { setCatFilter(e.target.value as string); setPage(1); }} data-testid="product-cat-filter">
            <MenuItem value="">{t('adminProducts.allCategories')}</MenuItem>
            {/* eslint-disable-next-line @typescript-eslint/no-explicit-any */}
            {categories.map((c: any) => <MenuItem key={c.id} value={c.name}>{c.name}</MenuItem>)}
          </Select>
        </FormControl>
      </Box>

      <Paper elevation={0} sx={{ borderRadius: 3, border: '1px solid #F1F5F9' }}>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: '#F8FAFC' }}>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>ID</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminProducts.name')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminProducts.category')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminProducts.price')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminProducts.stock')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminProducts.status')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminProducts.actions')}</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {products.map((p) => (
                <TableRow key={p.id}>
                  <TableCell sx={{ fontSize: 13 }}>{p.id}</TableCell>
                  <TableCell sx={{ fontSize: 13 }}>{p.name}</TableCell>
                  <TableCell sx={{ fontSize: 13 }}>{p.categoryName}</TableCell>
                  <TableCell sx={{ fontSize: 13 }}>{p.pointsPrice?.toLocaleString()}</TableCell>
                  <TableCell sx={{ fontSize: 13 }}>{p.stock}</TableCell>
                  <TableCell>
                    <Switch checked={p.status === 'active' || p.status === 'on'} onChange={() => handleToggle(p.id)} size="small" data-testid={`toggle-${p.id}`} />
                    <Chip label={p.status === 'active' || p.status === 'on' ? t('adminProducts.active') : t('adminProducts.inactive')} size="small"
                      sx={{ ml: 1, fontSize: 11, color: p.status === 'active' || p.status === 'on' ? '#166534' : '#64748B', bgcolor: p.status === 'active' || p.status === 'on' ? '#DCFCE7' : '#F1F5F9' }} />
                  </TableCell>
                  <TableCell>
                    <IconButton size="small" onClick={() => openEdit(p)} data-testid={`edit-${p.id}`}><EditIcon sx={{ fontSize: 18 }} /></IconButton>
                    <IconButton size="small" onClick={() => { setDeleteId(p.id); setDeleteOpen(true); }} data-testid={`delete-${p.id}`}><DeleteIcon sx={{ fontSize: 18, color: '#DC2626' }} /></IconButton>
                  </TableCell>
                </TableRow>
              ))}
              {products.length === 0 && <TableRow><TableCell colSpan={7} sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</TableCell></TableRow>}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      {total > size && <Box sx={{ display: 'flex', justifyContent: 'center' }}><Pagination count={Math.ceil(total / size)} page={page} onChange={(_, p) => setPage(p)} data-testid="product-pagination" /></Box>}

      {/* Create/Edit Dialog */}
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editItem ? t('adminProducts.edit') : t('adminProducts.add')}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '16px !important' }}>
          <TextField label={t('adminProducts.name')} value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} fullWidth size="small" data-testid="product-form-name" />
          <TextField label={t('adminProducts.description')} value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} fullWidth size="small" multiline rows={3} data-testid="product-form-desc" />
          <FormControl size="small" fullWidth>
            <InputLabel>{t('adminProducts.category')}</InputLabel>
            <Select value={form.categoryId} label={t('adminProducts.category')} onChange={(e) => setForm({ ...form, categoryId: e.target.value as string })} data-testid="product-form-cat">
              {/* eslint-disable-next-line @typescript-eslint/no-explicit-any */}
              {categories.map((c: any) => <MenuItem key={c.id} value={String(c.id)}>{c.name}</MenuItem>)}
            </Select>
          </FormControl>
          <TextField label={t('adminProducts.price')} type="number" value={form.pointsPrice} onChange={(e) => setForm({ ...form, pointsPrice: e.target.value })} fullWidth size="small" data-testid="product-form-price" />
          <TextField label={t('adminProducts.stock')} type="number" value={form.stock} onChange={(e) => setForm({ ...form, stock: e.target.value })} fullWidth size="small" data-testid="product-form-stock" />
          <TextField label={t('adminProducts.imageUrl')} value={form.imageUrl} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} fullWidth size="small" data-testid="product-form-image" />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>{t('common.cancel')}</Button>
          <Button variant="contained" onClick={handleSave} data-testid="product-form-save">{t('common.save')}</Button>
        </DialogActions>
      </Dialog>

      {/* Delete Confirm */}
      <Dialog open={deleteOpen} onClose={() => setDeleteOpen(false)}>
        <DialogTitle>{t('adminProducts.deleteTitle')}</DialogTitle>
        <DialogContent><Typography>{t('adminProducts.deleteConfirm')}</Typography></DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteOpen(false)}>{t('common.cancel')}</Button>
          <Button variant="contained" color="error" onClick={handleDelete} data-testid="confirm-delete">{t('common.delete')}</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
