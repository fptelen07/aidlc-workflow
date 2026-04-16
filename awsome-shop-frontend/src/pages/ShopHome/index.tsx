import { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Pagination from '@mui/material/Pagination';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import ShoppingBagIcon from '@mui/icons-material/ShoppingBag';
import Inventory2Icon from '@mui/icons-material/Inventory2';
import TollIcon from '@mui/icons-material/Toll';
import { productService } from '../../services/productService';
import { categoryService } from '../../services/categoryService';

export default function ShopHome() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [products, setProducts] = useState<any[]>([]);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [categories, setCategories] = useState<any[]>([]);
  const [activeCategory, setActiveCategory] = useState<string>('');
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);
  const size = 8;

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
        page, size,
        category: activeCategory || undefined,
      });
      const data = res?.data ?? res;
      setProducts(data?.records ?? data?.list ?? []);
      setTotal(data?.total ?? 0);
    } catch { setProducts([]); }
  }, [page, activeCategory]);

  useEffect(() => { fetchProducts(); }, [fetchProducts]);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, p: '24px 32px' }}>
      {/* Hero Banner */}
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', height: 160, borderRadius: '12px', px: '40px', background: 'linear-gradient(90deg, #2563EB 0%, #60A5FA 100%)' }}>
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
          <Typography sx={{ fontSize: 28, fontWeight: 700, color: '#fff' }}>{t('employee.heroTitle')}</Typography>
          <Typography sx={{ fontSize: 14, color: 'rgba(255,255,255,0.8)' }}>{t('employee.heroSubtitle')}</Typography>
          <Button size="small" endIcon={<ArrowForwardIcon sx={{ fontSize: 16 }} />}
            sx={{ bgcolor: '#fff', color: '#2563EB', borderRadius: '20px', px: '20px', py: '8px', fontSize: 13, fontWeight: 600, textTransform: 'none', alignSelf: 'flex-start', '&:hover': { bgcolor: '#f0f0f0' } }}>
            {t('employee.heroBrowse')}
          </Button>
        </Box>
        <ShoppingBagIcon sx={{ fontSize: 100, color: 'rgba(255,255,255,0.2)' }} />
      </Box>

      {/* Category Filter */}
      <Box sx={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
        <Chip label={t('employee.allCategories')} onClick={() => { setActiveCategory(''); setPage(1); }}
          sx={{ borderRadius: '20px', fontSize: 13, fontWeight: activeCategory === '' ? 600 : 400, color: activeCategory === '' ? '#fff' : '#64748B', bgcolor: activeCategory === '' ? '#2563EB' : '#fff', border: activeCategory === '' ? 'none' : '1px solid #E2E8F0', cursor: 'pointer' }} />
        {categories.map((cat) => (
          <Chip key={cat.id} label={cat.name} onClick={() => { setActiveCategory(cat.name); setPage(1); }}
            sx={{ borderRadius: '20px', fontSize: 13, fontWeight: activeCategory === cat.name ? 600 : 400, color: activeCategory === cat.name ? '#fff' : '#64748B', bgcolor: activeCategory === cat.name ? '#2563EB' : '#fff', border: activeCategory === cat.name ? 'none' : '1px solid #E2E8F0', cursor: 'pointer' }} />
        ))}
      </Box>

      {/* Product Grid */}
      <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '20px' }}>
        {products.map((product) => (
          <Card key={product.id} onClick={() => navigate(`/product/${product.id}`)}
            sx={{ borderRadius: '12px', border: '1px solid #F1F5F9', boxShadow: 'none', cursor: 'pointer', overflow: 'hidden', '&:hover': { boxShadow: 2 } }}>
            <Box sx={{ position: 'relative', height: 200, bgcolor: '#DBEAFE', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              {product.imageUrl ? <Box component="img" src={product.imageUrl} alt={product.name} sx={{ maxWidth: '100%', maxHeight: '100%', objectFit: 'contain' }} />
                : <Inventory2Icon sx={{ fontSize: 64, color: '#2563EB' }} />}
            </Box>
            <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', p: '16px', '&:last-child': { pb: '16px' } }}>
              <Typography sx={{ fontSize: 15, fontWeight: 600, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{product.name}</Typography>
              <Typography sx={{ fontSize: 12, color: 'text.secondary' }}>{product.categoryName}</Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <TollIcon sx={{ fontSize: 18, color: '#D97706' }} />
                  <Typography sx={{ fontSize: 18, fontWeight: 700, color: '#D97706' }}>{product.pointsPrice?.toLocaleString()}</Typography>
                </Box>
                <Button variant="contained" size="small" data-testid={`redeem-${product.id}`}
                  sx={{ borderRadius: '8px', px: '14px', py: '6px', fontSize: 13, fontWeight: 600, textTransform: 'none', minWidth: 'auto' }}>
                  {t('employee.redeem')}
                </Button>
              </Box>
            </CardContent>
          </Card>
        ))}
      </Box>

      {products.length === 0 && <Typography sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</Typography>}

      {total > size && <Box sx={{ display: 'flex', justifyContent: 'center' }}><Pagination count={Math.ceil(total / size)} page={page} onChange={(_, p) => setPage(p)} data-testid="shop-pagination" /></Box>}
    </Box>
  );
}
