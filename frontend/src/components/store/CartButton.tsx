"use client";

import { ShoppingCart } from "lucide-react";
import { Button } from "@/components/ui/button";

export function CartButton({ count, onClick }: { count: number; onClick: () => void }) {
  return (
    <Button variant="outline" className="relative" onClick={onClick}>
      <ShoppingCart className="h-5 w-5" />
      {count > 0 && (
        <span className="absolute -top-1 -right-1 text-xs bg-black text-white rounded-full px-1.5">
          {count}
        </span>
      )}
    </Button>
  );
}
