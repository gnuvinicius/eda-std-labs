"use client";

import { useEffect, useState } from "react";
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { ScrollArea } from "@/components/ui/scroll-area";

type Product = {
  id: string;
  name: string;
  description?: string;
  price: number;
  quantity?: number;
};

type CartCtx = {
  cart: Product[];
  open: boolean;
  setOpen: (v: boolean) => void;
  addToCart: (p: Product) => void;
  removeItem: (id: string) => void;
  updateQty: (id: string, qty: number) => void;
};

type Props = {
  // children MUST be a function that receives CartCtx
  children: (ctx: CartCtx) => React.ReactNode;
  initialCart?: Product[];
};

export function CartDrawer({ children, initialCart = [] }: Props) {
  const [open, setOpen] = useState(false);
  const [cart, setCart] = useState<Product[]>(initialCart);

  useEffect(() => {
    const openHandler = () => setOpen(true);
    window.addEventListener("open-cart", openHandler);
    return () => window.removeEventListener("open-cart", openHandler);
  }, []);

  const addToCart = (product: Product) => {
    setCart((prev) => {
      const exists = prev.find((p) => p.id === product.id);
      if (exists) {
        return prev.map((p) =>
          p.id === product.id ? { ...p, quantity: (p.quantity || 1) + 1 } : p
        );
      }
      return [...prev, { ...product, quantity: 1 }];
    });
    setOpen(true);
  };

  const removeItem = (id: string) => {
    setCart((prev) => prev.filter((p) => p.id !== id));
  };

  const updateQty = (id: string, qty: number) => {
    if (qty <= 0) return removeItem(id);
    setCart((prev) => prev.map((p) => (p.id === id ? { ...p, quantity: qty } : p)));
  };

  const ctx: CartCtx = { cart, open, setOpen, addToCart, removeItem, updateQty };

  const subtotal = cart.reduce((s, p) => s + p.price * (p.quantity || 1), 0);

  return (
    <>
      {/* Render children as a function with the cart context */}
      {children(ctx)}

      {/* The sheet (drawer) itself */}
      <Sheet open={open} onOpenChange={setOpen}>
        {/* We include a hidden trigger to keep Radix happy if required by your Sheet implementation */}
        <SheetTrigger asChild>
          <span />
        </SheetTrigger>

        <SheetContent side="right" className="w-96 p-0">
          <SheetHeader className="p-4 border-b">
            <SheetTitle>Carrinho</SheetTitle>
          </SheetHeader>

          <ScrollArea className="h-[calc(100vh-200px)] p-4">
            {cart.length === 0 && <p className="text-gray-500">Seu carrinho está vazio.</p>}

            {cart.map((item) => (
              <div key={item.id} className="flex items-start justify-between gap-3 py-3">
                <div className="flex-1">
                  <div className="flex items-center justify-between">
                    <p className="font-medium">{item.name}</p>
                    <p className="text-sm text-gray-600">R$ {(item.price).toFixed(2)}</p>
                  </div>

                  <div className="mt-2 flex items-center gap-2">
                    <Button size="sm" variant="outline" onClick={() => updateQty(item.id, (item.quantity || 1) - 1)}>-</Button>
                    <div className="px-3">{item.quantity || 1}</div>
                    <Button size="sm" variant="outline" onClick={() => updateQty(item.id, (item.quantity || 1) + 1)}>+</Button>

                    <Button size="sm" variant="ghost" onClick={() => removeItem(item.id)} className="ml-auto text-red-600">
                      Remover
                    </Button>
                  </div>
                </div>
              </div>
            ))}
          </ScrollArea>

          <Separator />

          <div className="p-4 flex flex-col gap-3">
            <div className="flex items-center justify-between">
              <span className="text-sm text-muted-foreground">Subtotal</span>
              <span className="font-medium">R$ {subtotal.toFixed(2)}</span>
            </div>

            <div className="flex gap-2">
              <Button className="flex-1" disabled={cart.length === 0} onClick={() => alert('Checkout: implementar integração com backend/Stripe')}>
                Finalizar Compra
              </Button>
              <Button variant="outline" onClick={() => { setCart([]); setOpen(false); }}>
                Limpar
              </Button>
            </div>
          </div>
        </SheetContent>
      </Sheet>
    </>
  );
}
